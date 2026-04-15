package com.smartcampus.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

public class DataStore {

    // Single instance of DataStore (Singleton Pattern)
    private static final DataStore INSTANCE = new DataStore();

    // Stores all rooms using room ID as key
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    // Stores all sensors using sensor ID as key
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    // Stores sensor readings (each sensor has a list of readings)
    private final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    // Private constructor so no other class can create object (Singleton)
    private DataStore() {

        // ----------- Creating Sample Rooms -----------
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Lab", 30);

        // Adding rooms to map
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        // ----------- Creating Sample Sensors -----------
        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "ACTIVE", 400.0, "LAB-101");

        // Adding sensors to map
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);

        // ----------- Linking Sensors to Rooms -----------
        r1.getSensorIds().add(s1.getId());
        r2.getSensorIds().add(s2.getId());

        // ----------- Initializing Reading Lists -----------
        // Each sensor will have its own list to store readings
        readings.put(s1.getId(), new ArrayList<>());
        readings.put(s2.getId(), new ArrayList<>());
    }

    // Method to get the single instance of DataStore
    public static DataStore getInstance() {
        return INSTANCE;
    }

    // Getter method to access all rooms
    public Map<String, Room> getRooms() { 
        return rooms; 
    }

    // Getter method to access all sensors
    public Map<String, Sensor> getSensors() { 
        return sensors; 
    }

    // Getter method to access all sensor readings
    public Map<String, List<SensorReading>> getReadings() { 
        return readings; 
    }
}