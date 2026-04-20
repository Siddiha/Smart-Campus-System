package com.smartcampus.exception.mappers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.smartcampus.exception.SensorUnavailableException;

/**
 * JAX-RS ExceptionMapper that handles SensorUnavailableException and converts it
 * into a structured HTTP 403 (Forbidden) JSON response.
 * 
 * This mapper is automatically registered with JAX-RS due to the @Provider annotation,
 * intercepting any SensorUnavailableException thrown from REST endpoints.
 */
@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {

    /**
     * Converts a SensorUnavailableException into a proper HTTP response.
     * 
     * @param exception The caught SensorUnavailableException containing the error message
     * @return A Response object with:
     *         - HTTP Status: 403 Forbidden
     *         - Content-Type: application/json
     *         - Body: JSON object with status, error type, and detailed message
     */
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        // Create a map to hold the error response structure
        Map<String, String> error = new HashMap<>();
        
        // Populate the error details
        error.put("status", "403 Forbidden");           // HTTP status description
        error.put("error", "Sensor Unavailable");       // Brief error category
        error.put("message", exception.getMessage());   // Specific error details from the exception
        
        // Build and return the JSON error response
        return Response.status(403)                     // HTTP 403 Forbidden status code
                .type(MediaType.APPLICATION_JSON)       // Set response content type to JSON
                .entity(error)                          // Attach the error map as response body
                .build();                               // Construct the final Response object
    }
}