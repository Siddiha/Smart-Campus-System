# Smart Campus System — REST API

A fully functional RESTful API built for the University of Westminster's **Smart Campus** initiative. The system manages physical **Rooms** and **Sensors** across campus, supporting full CRUD operations, historical sensor reading logs, filtered search, and robust error handling — all built using **JAX-RS (Jersey 2.41)** with an embedded **Grizzly HTTP Server**.

---

## Tech Stack

![Java](https://img.shields.io/badge/Java-11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Jersey](https://img.shields.io/badge/Jersey-2.41-4CAF50?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Grizzly](https://img.shields.io/badge/Grizzly-HTTP%20Server-0077B5?style=for-the-badge&logo=apache&logoColor=white)
![Jackson](https://img.shields.io/badge/Jackson-JSON-yellow?style=for-the-badge&logo=json&logoColor=white)
![REST](https://img.shields.io/badge/REST-API-blue?style=for-the-badge&logo=api&logoColor=white)

| Technology | Role in Project |
| --- | --- |
| **Java 11** | Core programming language |
| **JAX-RS (Jersey 2.41)** | Framework for building RESTful endpoints using annotations like `@GET`, `@POST`, `@Path` |
| **Grizzly HTTP Server** | Lightweight embedded server — no need for Tomcat or external deployment |
| **Jackson** | Automatic Java object ↔ JSON conversion (serialization/deserialization) |
| **Maven** | Dependency management and project build tool |
| **ConcurrentHashMap** | Thread-safe in-memory data store — simulates a database |

---

## What This System Does

The Smart Campus API acts as the backend brain for managing physical infrastructure on a university campus. Here is what it enables:

- **Room management** — Register new rooms, retrieve room details, and delete empty rooms
- **Sensor management** — Install sensors into rooms, filter by type (Temperature, CO2, etc.), and track their status
- **Sensor readings** — Record timestamped measurement values from sensors and retrieve the full reading history
- **Business rule enforcement** — Prevents invalid operations like deleting a room that still has sensors, or posting a reading to a sensor under maintenance
- **Error handling** — Every error returns a structured JSON response with a clear message, never a raw Java stack trace

---

## System Architecture

The project follows a clean layered architecture:

```text
HTTP Request
     ↓
[Grizzly HTTP Server]       ← Embedded server, started from Main.java
     ↓
[JAX-RS / Jersey Router]    ← Matches URL + HTTP method to resource class
     ↓
[Resource Layer]            ← Business logic (RoomResource, SensorResource, etc.)
     ↓
[DataStore (Singleton)]     ← In-memory storage using ConcurrentHashMap
     ↓
[Model Layer]               ← Plain Java objects: Room, Sensor, SensorReading
     ↑
[Exception Mappers]         ← Intercept thrown exceptions → return JSON error responses
```

### Key Design Patterns Used

#### 1. Singleton Pattern — `DataStore`
The `DataStore` class uses the **Singleton design pattern** — there is exactly one shared instance across the entire application, acting like a shared in-memory database.

```java
private static final DataStore INSTANCE = new DataStore();

public static DataStore getInstance() {
    return INSTANCE;
}
```

All resource classes call `DataStore.getInstance()` to access the same data. This guarantees consistency — a room created via the Rooms endpoint is immediately visible to the Sensors endpoint.

#### 2. Sub-Resource Locator Pattern
The `SensorResource` class uses a **sub-resource locator** to handle nested URLs like `/sensors/{sensorId}/readings`. Rather than mapping all logic into one class, it delegates to a dedicated `SensorReadingResource`:

```java
@Path("/{sensorId}/readings")
public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
    return new SensorReadingResource(sensorId);
}
```

This keeps each class focused and clean.

#### 3. Exception Mapper Pattern
Custom exceptions are thrown in business logic and **mapped to HTTP responses** by dedicated mapper classes annotated with `@Provider`. Jersey automatically intercepts the exception and calls the mapper:

```
throw new SensorUnavailableException("...")
       ↓
SensorUnavailableMapper.toResponse(exception)
       ↓
HTTP 403 JSON response
```

---

## Data Models

### Room
Represents a physical room on campus (classroom, lab, conference hall, etc.).

| Field | Type | Description |
| --- | --- | --- |
| `id` | String | Unique room identifier (e.g., `"LIB-301"`, `"LAB-101"`) |
| `name` | String | Human-readable display name (e.g., `"Library Quiet Study"`) |
| `capacity` | int | Maximum occupancy of the room |
| `sensorIds` | List\<String\> | IDs of all sensors installed in this room (auto-managed) |

Example JSON:
```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50,
  "sensorIds": ["TEMP-001"]
}
```

### Sensor
Represents a physical sensor device installed in a room.

| Field | Type | Description |
| --- | --- | --- |
| `id` | String | Unique sensor identifier (e.g., `"TEMP-001"`, `"CO2-001"`) |
| `type` | String | Sensor category: `Temperature`, `CO2`, `Humidity`, `Occupancy`, etc. |
| `status` | String | Operational state: `ACTIVE`, `MAINTENANCE`, or `OFFLINE` |
| `currentValue` | double | The most recent reading value — auto-updated when a new reading is posted |
| `roomId` | String | The room this sensor belongs to (must reference an existing room) |

Example JSON:
```json
{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}
```

**Sensor Status Values:**
- `ACTIVE` — Sensor is operational and accepts new readings
- `MAINTENANCE` — Sensor is under maintenance; posting new readings is **blocked** (returns HTTP 403)
- `OFFLINE` — Sensor is disconnected or not responding

### SensorReading
Represents a single timestamped measurement from a sensor.

| Field | Type | Description |
| --- | --- | --- |
| `id` | String | Auto-generated UUID (created server-side, no need to send it) |
| `timestamp` | long | Unix epoch timestamp in milliseconds (auto-set to server time) |
| `value` | double | The measured value (e.g., `22.5` for temperature in °C) |

When you POST a new reading, you only need to provide `value`. The `id` and `timestamp` are generated automatically by the server.

Example JSON (response):
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "timestamp": 1713600000000,
  "value": 24.3
}
```

---

## Pre-Seeded Data

When the server starts, the `DataStore` is pre-populated with the following sample data so you can test the API immediately without creating anything first:

**Rooms:**
| ID | Name | Capacity |
| --- | --- | --- |
| `LIB-301` | Library Quiet Study | 50 |
| `LAB-101` | Computer Lab | 30 |

**Sensors:**
| ID | Type | Status | Current Value | Room |
| --- | --- | --- | --- | --- |
| `TEMP-001` | Temperature | ACTIVE | 22.5 | LIB-301 |
| `CO2-001` | CO2 | ACTIVE | 400.0 | LAB-101 |

---

## API Reference

**Base URL:** `http://localhost:8081/api/v1`

All requests and responses use `Content-Type: application/json`.

---

### Discovery

#### `GET /api/v1/info`
Returns metadata about the API — useful for confirming the server is running.

**Response:** `200 OK`
```json
{
  "api": "Smart Campus REST API",
  "version": "1.0",
  "status": "running"
}
```

---

### Rooms

#### `GET /api/v1/rooms`
Returns a list of all rooms currently registered in the system.

**Response:** `200 OK`
```json
[
  {
    "id": "LIB-301",
    "name": "Library Quiet Study",
    "capacity": 50,
    "sensorIds": ["TEMP-001"]
  },
  {
    "id": "LAB-101",
    "name": "Computer Lab",
    "capacity": 30,
    "sensorIds": ["CO2-001"]
  }
]
```

---

#### `POST /api/v1/rooms`
Registers a new room.

**Request body:**
```json
{
  "id": "HALL-201",
  "name": "Main Hall",
  "capacity": 100
}
```

**Response:** `201 Created`
```json
{
  "id": "HALL-201",
  "name": "Main Hall",
  "capacity": 100,
  "sensorIds": []
}
```

**Validation rules:**
- `id` is required — returns `400 Bad Request` if missing
- `id` must be unique — returns `409 Conflict` if a room with that ID already exists

---

#### `GET /api/v1/rooms/{roomId}`
Retrieves a specific room by its ID.

**Path parameter:** `roomId` — the unique room identifier

**Response:** `200 OK` — the room object  
**Error:** `404 Not Found` if the room does not exist

```json
{
  "error": "Room 'XYZ-999' not found."
}
```

---

#### `DELETE /api/v1/rooms/{roomId}`
Deletes a room. The room must have no sensors linked to it.

**Path parameter:** `roomId` — the unique room identifier

**Response:** `200 OK`
```json
{
  "message": "Room 'HALL-201' deleted successfully."
}
```

**Business rule enforced:** If the room still has sensors assigned, deletion is **blocked**:
- Returns `409 Conflict` with a message listing how many sensors must be removed first

**Other errors:**
- `404 Not Found` — room does not exist

---

### Sensors

#### `GET /api/v1/sensors`
Returns all sensors. Optionally filter by sensor type using a query parameter.

**Optional query parameter:** `?type=<value>` — case-insensitive match on sensor type

Examples:
- `GET /api/v1/sensors` — returns all sensors
- `GET /api/v1/sensors?type=CO2` — returns only CO2 sensors
- `GET /api/v1/sensors?type=temperature` — case-insensitive, returns Temperature sensors

**Response:** `200 OK` — array of sensor objects

---

#### `POST /api/v1/sensors`
Registers a new sensor and links it to an existing room.

**Request body:**
```json
{
  "id": "HUM-001",
  "type": "Humidity",
  "status": "ACTIVE",
  "currentValue": 60.0,
  "roomId": "LIB-301"
}
```

**Response:** `201 Created` — the created sensor object

**What happens internally:**
1. Validates the sensor ID is provided and unique
2. Validates that `roomId` refers to an existing room (throws `LinkedResourceNotFoundException` if not)
3. Defaults `status` to `"ACTIVE"` if not provided
4. Adds the sensor to the DataStore
5. Initialises an empty readings list for this sensor
6. Adds the sensor's ID to the room's `sensorIds` list (bidirectional link)

**Validation rules:**
- `id` is required → `400 Bad Request`
- `id` must be unique → `409 Conflict`
- `roomId` must reference an existing room → `422 Unprocessable Entity`

---

#### `GET /api/v1/sensors/{sensorId}`
Retrieves a specific sensor by its ID.

**Response:** `200 OK` — the sensor object  
**Error:** `404 Not Found` if the sensor does not exist

---

### Sensor Readings

#### `GET /api/v1/sensors/{sensorId}/readings`
Returns the full reading history for a specific sensor, ordered by insertion time.

**Response:** `200 OK` — array of reading objects (empty array `[]` if no readings yet)  
**Error:** `404 Not Found` if the sensor does not exist

```json
[
  {
    "id": "a1b2c3d4-...",
    "timestamp": 1713600000000,
    "value": 22.5
  },
  {
    "id": "b2c3d4e5-...",
    "timestamp": 1713600060000,
    "value": 23.1
  }
]
```

---

#### `POST /api/v1/sensors/{sensorId}/readings`
Records a new reading for a sensor.

**Request body** (only `value` is required):
```json
{
  "value": 25.7
}
```

**Response:** `201 Created` — the stored reading with auto-generated `id` and `timestamp`

**What happens internally:**
1. Verifies the sensor exists → `404` if not
2. Checks the sensor's status — **blocks reading if `MAINTENANCE`** → throws `SensorUnavailableException` → `403 Forbidden`
3. Auto-generates a UUID for `id` and sets `timestamp` to current system time
4. Appends the reading to the sensor's reading history in the DataStore
5. Updates the sensor's `currentValue` with the new reading value

---

## Error Handling

The API never exposes raw Java stack traces. Every error returns a structured JSON body.

### Custom Exception Mappers

| Exception Class | HTTP Status | When It's Thrown |
| --- | --- | --- |
| `RoomNotEmptyException` | `409 Conflict` | Attempting to delete a room that still has sensors assigned |
| `LinkedResourceNotFoundException` | `422 Unprocessable Entity` | Creating a sensor with a `roomId` that does not exist |
| `SensorUnavailableException` | `403 Forbidden` | Posting a reading to a sensor with `MAINTENANCE` status |
| Any unhandled exception | `500 Internal Server Error` | Caught by `GlobalExceptionMapper` as a safety net |

### Error Response Format

All error responses follow this consistent JSON structure:

```json
{
  "error": "A human-readable description of what went wrong."
}
```

### How Exception Mappers Work

Jersey automatically scans for classes annotated with `@Provider` that implement `ExceptionMapper<T>`. When a resource method throws an exception, Jersey finds the matching mapper and calls its `toResponse()` method to build the HTTP response. This decouples error-handling logic from business logic.

Example flow for a maintenance sensor:
```
POST /sensors/TEMP-001/readings
        ↓
SensorReadingResource.addReading()
        ↓  sensor.status == "MAINTENANCE"
throw new SensorUnavailableException("Sensor is under MAINTENANCE...")
        ↓  Jersey intercepts it
SensorUnavailableMapper.toResponse(exception)
        ↓
HTTP 403 { "error": "Sensor 'TEMP-001' is currently under MAINTENANCE..." }
```

---

## Project Structure

```
Smart-Campus-System/
├── pom.xml                          ← Maven build config, all dependencies declared here
└── src/main/java/com/smartcampus/
    ├── Main.java                    ← Entry point: starts Grizzly server on port 8081
    ├── SmartCampusApplication.java  ← JAX-RS Application class, sets base path /api/v1
    │
    ├── model/                       ← Plain Java data objects (POJOs)
    │   ├── Room.java                ← Room with id, name, capacity, sensorIds
    │   ├── Sensor.java              ← Sensor with id, type, status, currentValue, roomId
    │   └── SensorReading.java       ← Reading with auto-UUID id, epoch timestamp, value
    │
    ├── resource/                    ← REST endpoint handlers (JAX-RS resource classes)
    │   ├── DiscoveryResource.java   ← GET /info — API metadata
    │   ├── RoomResource.java        ← GET/POST /rooms, GET/DELETE /rooms/{id}
    │   ├── SensorResource.java      ← GET/POST /sensors, GET /sensors/{id}, sub-resource locator
    │   └── SensorReadingResource.java ← GET/POST /sensors/{id}/readings
    │
    ├── storage/
    │   └── DataStore.java           ← Singleton in-memory store using ConcurrentHashMap
    │
    └── exception/
        ├── RoomNotEmptyException.java
        ├── LinkedResourceNotFoundException.java
        ├── SensorUnavailableException.java
        └── mappers/
            ├── RoomNotEmptyMapper.java           ← Maps to HTTP 409
            ├── LinkedResourceNotFoundMapper.java ← Maps to HTTP 422
            ├── SensorUnavailableMapper.java      ← Maps to HTTP 403
            └── GlobalExceptionMapper.java        ← Catch-all for HTTP 500
```

---

## How to Build and Run

### Prerequisites
- Java JDK 11 or higher
- Apache Maven 3.6+
- Apache NetBeans 12+ (or any Java IDE with Maven support)
- Postman or curl (for testing)

### Step-by-Step Instructions

**Step 1** — Clone the repository:
```bash
git clone https://github.com/YOUR_USERNAME/smart-campus-rest-api.git
```

**Step 2** — Open in Apache NetBeans:
- Launch Apache NetBeans
- Click **File** → **Open Project**
- Navigate to the cloned `Smart-Campus-System` folder
- Click **Open Project** — NetBeans detects it as a Maven project automatically

**Step 3** — Build the project:
- In the Projects panel, right-click the project → **Clean and Build**
- Maven downloads all dependencies on first run (may take a minute)
- Look for **BUILD SUCCESS** in the output window

**Step 4** — Run the server:
- Right-click the project → **Run**
- OR open [Main.java](src/main/java/com/smartcampus/Main.java) and click the green ▶ Run button
- Wait for this confirmation in the output window:
```
Smart Campus API is running at http://localhost:8081/api/v1
Press ENTER to stop the server...
```

**Step 5** — The API is live at:
```
http://localhost:8081/api/v1
```

**Step 6** — To stop the server:
- Press **ENTER** in the output window, or click the red ⏹ Stop button

> **Port conflict fix:** If you see `Address already in use`, run in Command Prompt:
> ```
> netstat -ano | findstr :8081
> taskkill /PID <number> /F
> ```
> Then run the project again.

---

## Testing with curl

All commands below assume the server is running on `http://localhost:8081`.

### Discovery

```bash
# Check the API is running
curl -X GET http://localhost:8081/api/v1/info
```

### Rooms

```bash
# List all rooms (includes pre-seeded LIB-301 and LAB-101)
curl -X GET http://localhost:8081/api/v1/rooms

# Get a specific room
curl -X GET http://localhost:8081/api/v1/rooms/LIB-301

# Create a new room
curl -X POST http://localhost:8081/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"HALL-201\",\"name\":\"Main Hall\",\"capacity\":100}"

# Delete a room (only works if it has no sensors)
curl -X DELETE http://localhost:8081/api/v1/rooms/HALL-201

# Attempt to delete a room that has sensors → expect 409 Conflict
curl -X DELETE http://localhost:8081/api/v1/rooms/LIB-301
```

### Sensors

```bash
# List all sensors
curl -X GET http://localhost:8081/api/v1/sensors

# Filter sensors by type (case-insensitive)
curl -X GET "http://localhost:8081/api/v1/sensors?type=CO2"
curl -X GET "http://localhost:8081/api/v1/sensors?type=temperature"

# Get a specific sensor
curl -X GET http://localhost:8081/api/v1/sensors/TEMP-001

# Register a new sensor in an existing room
curl -X POST http://localhost:8081/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"HUM-001\",\"type\":\"Humidity\",\"status\":\"ACTIVE\",\"currentValue\":60.0,\"roomId\":\"LIB-301\"}"

# Attempt to register sensor in a non-existent room → expect 422
curl -X POST http://localhost:8081/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"HUM-002\",\"type\":\"Humidity\",\"status\":\"ACTIVE\",\"currentValue\":55.0,\"roomId\":\"FAKE-999\"}"
```

### Sensor Readings

```bash
# Get all readings for a sensor (empty list initially)
curl -X GET http://localhost:8081/api/v1/sensors/TEMP-001/readings

# Add a new reading (only provide value — id and timestamp are auto-generated)
curl -X POST http://localhost:8081/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":24.3}"

# Add another reading and check that currentValue updates on the sensor
curl -X POST http://localhost:8081/api/v1/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":450.0}"
```

### Triggering Business Rule Errors

```bash
# 409 — Delete a room with sensors
curl -X DELETE http://localhost:8081/api/v1/rooms/LIB-301

# 422 — Create sensor with non-existent roomId
curl -X POST http://localhost:8081/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"TEST-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"GHOST-000\"}"

# 403 — Post reading to a MAINTENANCE sensor
# First create a sensor in maintenance mode
curl -X POST http://localhost:8081/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"MNT-001\",\"type\":\"Temperature\",\"status\":\"MAINTENANCE\",\"currentValue\":0.0,\"roomId\":\"LAB-101\"}"

# Then try posting a reading to it
curl -X POST http://localhost:8081/api/v1/sensors/MNT-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":19.0}"
```

---

## Important Notes

- **In-memory storage only** — all data is lost when the server stops. There is no database or file persistence.
- **No authentication** — this API is designed for academic/coursework purposes and has no security layer.
- **Thread safety** — `ConcurrentHashMap` is used throughout `DataStore`, making the API safe for concurrent requests.
- **Auto-linking** — when a sensor is created, it is automatically added to the room's `sensorIds` list. This bidirectional link is maintained in-memory.
- **currentValue auto-update** — every time a new reading is posted to a sensor, the sensor's `currentValue` field is updated to reflect the latest measurement.

---

## Module Information

- **Module:** 5COSC022W Client-Server Architectures
- **University:** University of Westminster
- **Academic Year:** 2025/26
