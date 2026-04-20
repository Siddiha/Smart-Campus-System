package com.smartcampus.model;

/**
 * Represents a physical sensor device installed in a room within the smart campus system.
 * Each sensor has an identifier, type, operational status, current reading value,
 * and a reference to the room where it is located.
 */
public class Sensor {
    private String id;           // Unique identifier for the sensor device
    private String type;         // Type of sensor (e.g., "TEMPERATURE", "HUMIDITY", "CO2", "MOTION")
    private String status;       // Operational status: ACTIVE, MAINTENANCE, or OFFLINE
    private double currentValue; // Most recent reading/value from the sensor
    private String roomId;       // ID of the room where this sensor is installed

    /**
     * Default constructor required for frameworks like JSON serialization,
     * ORM, or bean manipulation tools.
     */
    public Sensor() {}

    /**
     * Constructs a new Sensor with all properties specified.
     * 
     * @param id            Unique identifier for the sensor
     * @param type          Type of sensor (e.g., "TEMPERATURE", "HUMIDITY")
     * @param status        Operational status (ACTIVE, MAINTENANCE, OFFLINE)
     * @param currentValue  Most recent reading from the sensor
     * @param roomId        ID of the room where the sensor is located
     */
    public Sensor(String id, String type, String status, double currentValue, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
        this.roomId = roomId;
    }

    /**
     * Returns the unique identifier of the sensor.
     * @return The sensor ID as a String
     */
    public String getId() { 
        return id; 
    }

    /**
     * Sets the unique identifier of the sensor.
     * @param id The new ID to assign
     */
    public void setId(String id) { 
        this.id = id; 
    }

    /**
     * Returns the type of this sensor.
     * @return Sensor type (e.g., "TEMPERATURE", "HUMIDITY", "CO2", "MOTION")
     */
    public String getType() { 
        return type; 
    }

    /**
     * Sets the type of this sensor.
     * @param type The sensor type to assign
     */
    public void setType(String type) { 
        this.type = type; 
    }

    /**
     * Returns the operational status of the sensor.
     * @return Status string: ACTIVE, MAINTENANCE, or OFFLINE
     */
    public String getStatus() { 
        return status; 
    }

    /**
     * Sets the operational status of the sensor.
     * @param status The new status (should be ACTIVE, MAINTENANCE, or OFFLINE)
     */
    public void setStatus(String status) { 
        this.status = status; 
    }

    /**
     * Returns the most recent reading/value from the sensor.
     * @return The current sensor value as a double
     */
    public double getCurrentValue() { 
        return currentValue; 
    }

    /**
     * Updates the most recent reading/value from the sensor.
     * @param currentValue The new sensor reading to store
     */
    public void setCurrentValue(double currentValue) { 
        this.currentValue = currentValue; 
    }

    /**
     * Returns the ID of the room where this sensor is installed.
     * @return The room ID as a String
     */
    public String getRoomId() { 
        return roomId; 
    }

    /**
     * Sets the room ID where this sensor is installed.
     * @param roomId The room ID to associate with this sensor
     */
    public void setRoomId(String roomId) { 
        this.roomId = roomId; 
    }
}