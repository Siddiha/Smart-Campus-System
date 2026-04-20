package com.smartcampus.exception.mappers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Global JAX-RS ExceptionMapper that handles any uncaught Throwable (exceptions and errors)
 * from REST endpoints. This acts as a "catch-all" for unexpected errors that aren't
 * handled by specific exception mappers.
 * 
 * This ensures that clients always receive a consistent JSON error response instead
 * of HTML error pages or raw exception stack traces.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    /**
     * Converts any uncaught Throwable into a proper HTTP response.
     * 
     * Special handling for WebApplicationException (JAX-RS native exceptions):
     * - These already contain a valid Response object
     * - We return that response directly without modification
     * 
     * For all other exceptions:
     * - Returns HTTP 500 Internal Server Error
     * - Returns a generic error message (hides implementation details for security)
     * - Logging should be implemented separately to track actual errors
     * 
     * @param exception The uncaught Throwable (Exception or Error)
     * @return A Response object with appropriate HTTP status and JSON error body
     */
    @Override
    public Response toResponse(Throwable exception) {
        // Let JAX-RS handle its own exceptions normally (e.g., NotFoundException, NotAllowedException)
        // These already have proper HTTP status codes and responses
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        // Create a map to hold the error response structure
        Map<String, String> error = new HashMap<>();
        
        // Populate the error details with generic messages (no internal details exposed to client)
        error.put("status", "500 Internal Server Error");              // HTTP status description
        error.put("error", "Unexpected Server Error");                 // Brief error category
        error.put("message", "An unexpected error occurred. Please contact the system administrator."); // User-friendly message
        
        // Build and return the JSON error response
        return Response.status(500)                     // HTTP 500 Internal Server Error status code
                .type(MediaType.APPLICATION_JSON)       // Set response content type to JSON
                .entity(error)                          // Attach the error map as response body
                .build();                               // Construct the final Response object
    }
}