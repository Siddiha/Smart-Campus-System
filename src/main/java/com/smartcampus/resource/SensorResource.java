package com.smartcampus.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.storage.DataStore;

// Base path for this resource → /sensors
@Path("/sensors")

// Specifies that responses will be in JSON format
@Produces(MediaType.APPLICATION_JSON)

// Specifies that requests should be in JSON format
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // Getting the single instance of DataStore (Singleton)
    private final DataStore store = DataStore.getInstance();

    // ----------- GET ALL SENSORS -----------
    // GET /api/v1/sensors
    // GET /api/v1/sensors?type=CO2 (filter by type)
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {

        // Get all sensors from datastore
        Collection<Sensor> all = store.getSensors().values();

        // If type is provided → filter sensors by type
        if (type != null && !type.isBlank()) {
            List<Sensor> filtered = all.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());

            return Response.ok(filtered).build();
        }

        // Return all sensors if no filter
        return Response.ok(all).build();
    }

    // ----------- CREATE NEW SENSOR -----------
    // POST /api/v1/sensors
    @POST
    public Response createSensor(Sensor sensor) {

        // Check if sensor ID is provided
        if (sensor.getId() == null || sensor.getId().isBlank()) {
            return Response.status(400)
                    .entity(errorBody("Sensor ID is required."))
                    .build();
        }

        // Check if sensor already exists
        if (store.getSensors().containsKey(sensor.getId())) {
            return Response.status(409)
                    .entity(errorBody("Sensor '" + sensor.getId() + "' already exists."))
                    .build();
        }

        // Validate if the given room exists
        if (sensor.getRoomId() == null || !store.getRooms().containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room '" + sensor.getRoomId() + "' does not exist. Cannot register sensor."
            );
        }

        // Set default status if not provided
        if (sensor.getStatus() == null || sensor.getStatus().isBlank()) {
            sensor.setStatus("ACTIVE");
        }

        // Add sensor to datastore
        store.getSensors().put(sensor.getId(), sensor);

        // Initialize empty readings list for this sensor
        store.getReadings().put(sensor.getId(), new ArrayList<>());

        // Link sensor to the corresponding room
        Room room = store.getRooms().get(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        // Return created sensor with HTTP 201 status
        return Response.status(201).entity(sensor).build();
    }

    // ----------- GET SENSOR BY ID -----------
    // GET /api/v1/sensors/{sensorId}
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {

        // Retrieve sensor from datastore
        Sensor sensor = store.getSensors().get(sensorId);

        // If not found → return 404
        if (sensor == null) {
            return Response.status(404)
                    .entity(errorBody("Sensor '" + sensorId + "' not found."))
                    .build();
        }

        // Return sensor if found
        return Response.ok(sensor).build();
    }

    // ----------- SUB-RESOURCE LOCATOR -----------
    // Handles /api/v1/sensors/{sensorId}/readings
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {

        // Delegates request to SensorReadingResource class
        return new SensorReadingResource(sensorId);
    }

    // ----------- HELPER METHOD FOR ERROR RESPONSE -----------
    // Creates a simple JSON error message
    private Map<String, String> errorBody(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}