package com.smartcampus.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.storage.DataStore;

// Specifies that this resource returns JSON responses
@Produces(MediaType.APPLICATION_JSON)

// Specifies that this resource accepts JSON requests
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    // Stores the sensor ID from the parent resource (SensorResource)
    private final String sensorId;

    // Accessing the shared DataStore (Singleton)
    private final DataStore store = DataStore.getInstance();

    // Constructor → receives sensorId from sub-resource locator
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // ----------- GET ALL READINGS FOR A SENSOR -----------
    // GET /api/v1/sensors/{sensorId}/readings
    @GET
    public Response getReadings() {

        // Check if the sensor exists
        Sensor sensor = store.getSensors().get(sensorId);

        // If sensor not found → return 404 error
        if (sensor == null) {
            return Response.status(404)
                    .entity(errorBody("Sensor '" + sensorId + "' not found."))
                    .build();
        }

        // Get reading history for the sensor
        // If no readings exist → return empty list
        List<SensorReading> history = store.getReadings()
                .getOrDefault(sensorId, List.of());

        // Return readings with HTTP 200 OK
        return Response.ok(history).build();
    }

    // ----------- ADD NEW READING TO SENSOR -----------
    // POST /api/v1/sensors/{sensorId}/readings
    @POST
    public Response addReading(SensorReading reading) {

        // Check if the sensor exists
        Sensor sensor = store.getSensors().get(sensorId);

        // If sensor not found → return 404 error
        if (sensor == null) {
            return Response.status(404)
                    .entity(errorBody("Sensor '" + sensorId + "' not found."))
                    .build();
        }

        // ----------- BUSINESS RULE VALIDATION -----------
        // If sensor is under MAINTENANCE → block new readings
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is currently under MAINTENANCE and cannot accept new readings."
            );
        }

        // ----------- AUTO-GENERATE READING DETAILS -----------
        // If reading ID is missing → create new reading with auto ID & timestamp
        if (reading.getId() == null || reading.getId().isBlank()) {
            reading = new SensorReading(reading.getValue());
        }

        // ----------- STORE THE READING -----------
        // Add reading to sensor's reading list
        // computeIfAbsent → creates list if it doesn't exist
        store.getReadings()
             .computeIfAbsent(sensorId, k -> new java.util.ArrayList<>())
             .add(reading);

        // ----------- UPDATE SENSOR STATE -----------
        // Update the sensor's current value with latest reading
        sensor.setCurrentValue(reading.getValue());

        // Return created reading with HTTP 201 status
        return Response.status(201).entity(reading).build();
    }

    // ----------- HELPER METHOD FOR ERROR RESPONSES -----------
    // Creates a simple JSON error message
    private Map<String, String> errorBody(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}