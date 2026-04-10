package com.smartcampus.exception.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        Map<String, String> error = new HashMap<>();
        error.put("status", "500 Internal Server Error");
        error.put("error", "Unexpected Server Error");
        error.put("message", "An unexpected error occurred. Please contact the system administrator.");
        // We intentionally do NOT expose exception.getMessage() or stack traces to the client
        return Response.status(500)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
