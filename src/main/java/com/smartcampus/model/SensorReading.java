package com.smartcampus.model;

import java.util.UUID;

/**
 * Represents a single reading from a sensor in the smart campus system.
 * Each reading has a unique identifier, a timestamp (milliseconds since epoch),
 * and the measured value.
 */
public class SensorReading {
    private String id;        // Unique identifier for this reading
    private long timestamp;   // Time when the reading was taken (milliseconds since Unix epoch)
    private double value;     // The sensor measurement value

    /**
     * Default constructor required for frameworks like JSON serialization,
     * ORM, or bean manipulation tools.
     */
    public SensorReading() {}

    /**
     * Constructs a new sensor reading with an automatically generated ID
     * and the current system time as the timestamp.
     * 
     * @param value The sensor measurement value to store
     */
    public SensorReading(double value) {
        this.id = UUID.randomUUID().toString();  // Generate random unique ID
        this.timestamp = System.currentTimeMillis(); // Capture current time in milliseconds
        this.value = value;
    }

    /**
     * Returns the unique identifier of this reading.
     * @return The reading ID as a String
     */
    public String getId() { 
        return id; 
    }

    /**
     * Sets the unique identifier of this reading.
     * @param id The new ID to assign
     */
    public void setId(String id) { 
        this.id = id; 
    }

    /**
     * Returns the timestamp of this reading.
     * @return Timestamp in milliseconds since Unix epoch
     */
    public long getTimestamp() { 
        return timestamp; 
    }

    /**
     * Sets the timestamp of this reading.
     * @param timestamp Timestamp in milliseconds since Unix epoch
     */
    public void setTimestamp(long timestamp) { 
        this.timestamp = timestamp; 
    }

    /**
     * Returns the sensor measurement value.
     * @return The recorded value as a double
     */
    public double getValue() { 
        return value; 
    }

    /**
     * Sets the sensor measurement value.
     * @param value The new value to store
     */
    public void setValue(double value) { 
        this.value = value; 
    }
}