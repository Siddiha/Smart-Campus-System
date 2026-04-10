package com.smartcampus.exception.mappers;

import com.smartcampus.exception.RoomNotEmptyException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("status", "409 Conflict");
        error.put("error", "Room Not Empty");
        error.put("message", exception.getMessage());
        return Response.status(409)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
