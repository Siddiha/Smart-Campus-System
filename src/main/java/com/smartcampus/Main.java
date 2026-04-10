package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://0.0.0.0:8080/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.smartcampus");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws Exception {
        final HttpServer server = startServer();
        System.out.println("Smart Campus API is running at http://localhost:8080/api/v1");
        System.out.println("Press ENTER to stop the server...");
        System.in.read();
        server.stop();
    }
}
