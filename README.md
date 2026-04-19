#  Smart Campus System

A fully functional RESTful API built for the University of Westminster's "Smart Campus" initiative. This API manages Rooms and Sensors across the campus, built using JAX-RS (Jersey 2.41) with an embedded Grizzly HTTP Server.

---

## 🛠️ Tech Stack

![Java](https://img.shields.io/badge/Java-11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Jersey](https://img.shields.io/badge/Jersey-2.41-4CAF50?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Grizzly](https://img.shields.io/badge/Grizzly-HTTP%20Server-0077B5?style=for-the-badge&logo=apache&logoColor=white)
![Jackson](https://img.shields.io/badge/Jackson-JSON-yellow?style=for-the-badge&logo=json&logoColor=white)
![REST](https://img.shields.io/badge/REST-API-blue?style=for-the-badge&logo=api&logoColor=white)

---

## 📌 API Overview

This API provides a seamless interface for campus facilities managers and automated building systems to interact with campus data. It supports full CRUD operations for Rooms and Sensors, historical sensor reading logs, filtered search, and comprehensive error handling with custom exception mappers.

**Base URL:** `http://localhost:8081/api/v1`

### 📋 Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/info | API discovery and metadata |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a new room |
| GET | /api/v1/rooms/{roomId} | Get a specific room by ID |
| DELETE | /api/v1/rooms/{roomId} | Delete a room (only if no sensors) |
| GET | /api/v1/sensors | Get all sensors (supports ?type= filter) |
| POST | /api/v1/sensors | Register a new sensor |
| GET | /api/v1/sensors/{sensorId} | Get a specific sensor by ID |
| GET | /api/v1/sensors/{sensorId}/readings | Get all readings for a sensor |
| POST | /api/v1/sensors/{sensorId}/readings | Add a new reading for a sensor |

## 📁 Project Structure

```
smart-campus-rest-api/
├── pom.xml
└── src/main/java/com/smartcampus/
    ├── Main.java
    ├── SmartCampusApplication.java
    ├── model/
    │   ├── Room.java
    │   ├── Sensor.java
    │   └── SensorReading.java
    ├── resource/
    │   ├── DiscoveryResource.java
    │   ├── RoomResource.java
    │   ├── SensorResource.java
    │   └── SensorReadingResource.java
    ├── storage/
    │   └── DataStore.java
    └── exception/
        ├── RoomNotEmptyException.java
        ├── LinkedResourceNotFoundException.java
        ├── SensorUnavailableException.java
        └── mappers/
            ├── RoomNotEmptyMapper.java
            ├── LinkedResourceNotFoundMapper.java
            ├── SensorUnavailableMapper.java
            └── GlobalExceptionMapper.java
```

## ⚙️ How to Build and Run

### ✅ Prerequisites
- Java JDK 11 or higher
- Apache Maven 3.6+
- Apache NetBeans 12+ (or any Java IDE)
- Postman (for testing)

### 📝 Step-by-Step Instructions

**Step 1** — Clone the repository from GitHub:
```bash
git clone https://github.com/YOUR_USERNAME/smart-campus-rest-api.git
```

**Step 2** — Open the project in Apache NetBeans:
- Launch Apache NetBeans
- Click **File** → **Open Project**
- Navigate to the cloned `smart-campus-rest-api` folder
- Click **Open Project**
- NetBeans will automatically detect it as a Maven project

**Step 3** — Download dependencies and build:
- In the Projects panel, right-click the project
- Click **Clean and Build**
- Wait for Maven to download all dependencies (first time may take a few minutes)
- You should see **BUILD SUCCESS** in the output window

**Step 4** — Run the server:
- Right-click the project → **Run**
- OR open `Main.java` and click the green ▶ Run button
- Wait for this message in the output window:
Smart Campus API is running at http://localhost:8081/api/v1
Press ENTER to stop the server...

**Step 5** — The API is now live at:
http://localhost:8081/api/v1

**Step 6** — To stop the server:
- Click the red ⏹ Stop button in the NetBeans output panel
- OR press ENTER in the output window

> ⚠️ **Important:** If you get "Address already in use" error, open Command Prompt and run:
> ```
> netstat -ano | findstr :8081
> taskkill /PID <number> /F
> ```
> Then run the project again.

---

## 🧪 Sample curl Commands

### 1. Get API discovery info
```bash
curl -X GET http://localhost:8081/api/v1/info
```

### 2. Get all rooms
```bash
curl -X GET http://localhost:8081/api/v1/rooms
```

### 3. Create a new room
```bash
curl -X POST http://localhost:8081/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"HALL-201\",\"name\":\"Main Hall\",\"capacity\":100}"
```

### 4. Get a specific room by ID
```bash
curl -X GET http://localhost:8081/api/v1/rooms/LIB-301
```

### 5. Delete a room that has no sensors
```bash
curl -X DELETE http://localhost:8081/api/v1/rooms/HALL-201
```

### 6. Get all sensors
```bash
curl -X GET http://localhost:8081/api/v1/sensors
```

### 7. Get sensors filtered by type
```bash
curl -X GET "http://localhost:8081/api/v1/sensors?type=CO2"
```

### 8. Create a new sensor
```bash
curl -X POST http://localhost:8081/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"OCC-001\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"LIB-301\"}"
```

### 9. Add a sensor reading
```bash
curl -X POST http://localhost:8081/api/v1/sensors/OCC-001/readings -H "Content-Type: application/json" -d "{\"value\":42.5}"
```

### 10. Get all readings for a sensor
```bash
curl -X GET http://localhost:8081/api/v1/sensors/TEMP-001/readings
```

---

## ❌ Error Handling

The API handles all errors gracefully and never exposes raw Java stack traces.

| Exception | HTTP Status | Scenario |
|-----------|-------------|----------|
| RoomNotEmptyException | 409 Conflict | Deleting a room that still has sensors |
| LinkedResourceNotFoundException | 422 Unprocessable Entity | Creating a sensor with a non-existent roomId |
| SensorUnavailableException | 403 Forbidden | Posting a reading to a sensor in MAINTENANCE status |
| Any unexpected error | 500 Internal Server Error | Caught by global exception mapper |

---

## 👨‍💻 Siddiha

- **Module:** 5COSC022W Client-Server Architectures
- **University:** University of Westminster
- **Year:** 2025/26

