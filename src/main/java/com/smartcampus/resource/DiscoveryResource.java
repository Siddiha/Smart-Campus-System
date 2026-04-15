package com.smartcampus.resource;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// ----------- ROOT RESOURCE (API DISCOVERY ENDPOINT) -----------
// This class provides a simple entry point to the API.
// When users access the base URL, they get general API information.

@Path("/") // Base path → accessed when user calls root URL (e.g., /api/v1/)
public class DiscoveryResource {

    // ----------- DISCOVERY ENDPOINT -----------
    // GET /
    // Returns basic API details and available resources
    @GET

    // Specifies that response will be in JSON format
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {

        // ----------- MAIN RESPONSE OBJECT -----------
        // Using a Map to build JSON response dynamically
        Map<String, Object> info = new HashMap<>();

        // Adding basic API information
        info.put("api", "Smart Campus Sensor & Room Management API"); // API name
        info.put("version", "1.0"); // API version
        info.put("contact", "admin@smartcampus.ac.uk"); // Contact email
        info.put("status", "running"); // Current API status

        // ----------- HATEOAS STYLE LINKS -----------
        // Providing links to available resources in the API
        Map<String, String> links = new HashMap<>();

        // Key = resource name, Value = endpoint path
        links.put("rooms", "/api/v1/rooms");     // Rooms resource endpoint
        links.put("sensors", "/api/v1/sensors"); // Sensors resource endpoint

        // Adding links to main response
        info.put("resources", links);

        // ----------- RETURN RESPONSE -----------
        // Response.ok() → HTTP 200 OK
        // info → converted into JSON automatically
        return Response.ok(info).build();
    }
}