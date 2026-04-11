package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a physical room on campus (classroom, lab, conference hall, etc.)
 * Each room can have multiple sensors associated with it for monitoring.
 */
public class Room {
    private String id;           // Unique room identifier (e.g., "R101")
    private String name;         // Display name of the room (e.g., "Computer Lab")
    private int capacity;        // Maximum number of people the room can hold
    private List<String> sensorIds = new ArrayList<>();  // IDs of sensors installed in this room

    // Default constructor required for JSON serialization
    public Room() {}

    // Constructor for creating a room with basic details (sensor IDs can be added later)
    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<String> getSensorIds() { return sensorIds; }
    public void setSensorIds(List<String> sensorIds) { this.sensorIds = sensorIds; }
}