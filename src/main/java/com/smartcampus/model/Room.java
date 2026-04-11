package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a physical room on campus (classroom, lecture hall, lab, conference room, etc.).
 * Each room can have multiple sensors (temperature, occupancy, etc.) associated with it.
 * 
 * @author Smart Campus Team
 * @version 1.0
 */
public class Room {
    
    // ===== Core Properties =====
    
    /** Unique identifier for the room (e.g., "R101", "CS-LAB-202") */
    private String id;
    
    /** Human-readable room name (e.g., "Computer Science Lab", "Hall 1") */
    private String name;
    
    /** Maximum number of people the room can accommodate */
    private int capacity;
    
    /** List of sensor IDs installed in this room for monitoring various parameters */
    private List<String> sensorIds = new ArrayList<>();

    // ===== Constructors =====
    
    /**
     * Default constructor required for JSON serialization/deserialization.
     * Frameworks like Jersey/JAX-RS need this to create Room objects from API requests.
     */
    public Room() {}

    /**
     * Constructor to create a room with essential details.
     * sensorIds list is initialized as empty and can be populated later.
     * 
     * @param id       Unique room identifier (e.g., "R101")
     * @param name     Display name of the room (e.g., "Conference Hall A")
     * @param capacity Maximum seating capacity of the room
     */
    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        // sensorIds is automatically initialized as empty ArrayList
    }

    // ===== Getters and Setters =====
    
    /**
     * Gets the unique room identifier.
     * @return Room ID (e.g., "R101")
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Sets the unique room identifier.
     * @param id Room identifier - should be unique across all rooms
     */
    public void setId(String id) { 
        this.id = id; 
    }

    /**
     * Gets the display name of the room.
     * @return Room name (e.g., "Main Lecture Hall")
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Sets the display name of the room.
     * @param name Room name for display purposes
     */
    public void setName(String name) { 
        this.name = name; 
    }

    /**
     * Gets the maximum capacity of the room.
     * @return Maximum number of people allowed
     */
    public int getCapacity() { 
        return capacity; 
    }
    
    /**
     * Sets the maximum capacity of the room.
     * @param capacity Maximum occupancy limit
     */
    public void setCapacity(int capacity) { 
        this.capacity = capacity; 
    }

    /**
     * Gets the list of sensor IDs installed in this room.
     * Each sensor ID corresponds to a Sensor object in the system.
     * 
     * @return List of sensor IDs (may be empty if no sensors installed)
     * 
     * Example usage:
     * Room room = new Room("R101", "CS Lab", 30);
     * room.getSensorIds().add("SENSOR_TEMP_01");
     * room.getSensorIds().add("SENSOR_OCC_05");
     */
    public List<String> getSensorIds() { 
        return sensorIds; 
    }
    
    /**
     * Sets the complete list of sensor IDs for this room.
     * Replaces any existing sensor associations.
     * 
     * @param sensorIds New list of sensor IDs to associate with this room
     */
    public void setSensorIds(List<String> sensorIds) { 
        this.sensorIds = sensorIds; 
    }
    
    // ===== Utility Methods (Optional - can be added) =====
    
    /**
     * Adds a single sensor to this room.
     * 
     * @param sensorId ID of the sensor to add
     * @return true if sensor was added successfully
     */
    public boolean addSensor(String sensorId) {
        return this.sensorIds.add(sensorId);
    }
    
    /**
     * Removes a sensor from this room.
     * 
     * @param sensorId ID of the sensor to remove
     * @return true if sensor was found and removed
     */
    public boolean removeSensor(String sensorId) {
        return this.sensorIds.remove(sensorId);
    }
    
    /**
     * Checks if this room has a specific sensor installed.
     * 
     * @param sensorId ID of the sensor to check
     * @return true if the sensor is installed in this room
     */
    public boolean hasSensor(String sensorId) {
        return this.sensorIds.contains(sensorId);
    }
    
    /**
     * Returns the number of sensors installed in this room.
     * @return Count of associated sensors
     */
    public int getSensorCount() {
        return this.sensorIds.size();
    }
    
    @Override
    public String toString() {
        return String.format("Room{id='%s', name='%s', capacity=%d, sensors=%d}", 
                             id, name, capacity, sensorIds.size());
    }
}