package com.smartcampus;

// Import Grizzly HTTP server classes - Grizzly is a lightweight HTTP server
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class to start the Smart Campus REST API server.
 * Uses Grizzly (lightweight HTTP server) with Jersey (JAX-RS implementation).
 */
public class Main {

    /**
     * Base URI where the API will be accessible.
     * 0.0.0.0 means listen on all network interfaces (local and remote access).
     * Port 8081 - API runs on this port.
     * /api/v1/ - API version 1 base path.
     */
    public static final String BASE_URI = "http://0.0.0.0:8081/api/v1/";

    /**
     * Starts and configures the HTTP server.
     * 
     * @return HttpServer instance that is running and accepting requests
     */
    public static HttpServer startServer() {
        // Create ResourceConfig - tells Jersey where to find REST resource classes
        // .packages("com.smartcampus") - scans all classes in this package and subpackages
        // for @Path, @GET, @POST annotations to register as REST endpoints
        final ResourceConfig rc = new ResourceConfig().packages("com.smartcampus");
        
        // Create and start Grizzly HTTP server at BASE_URI with our resource configuration
        // This binds the REST endpoints to the server
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method - application entry point.
     * Starts the server and keeps it running until user presses ENTER.
     * 
     * @param args Command line arguments (not used)
     * @throws Exception if server fails to start
     */
    public static void main(String[] args) throws Exception {
        // Start the HTTP server (this starts listening for requests)
        final HttpServer server = startServer();
        
        // Print confirmation message with access URL
        // Using localhost instead of 0.0.0.0 for user convenience
        System.out.println("Smart Campus API is running at http://localhost:8081/api/v1");
        System.out.println("Press ENTER to stop the server...");
        
        // Wait for user to press ENTER (blocks the main thread)
        // Without this, the program would exit immediately
        System.in.read();
        
        // Stop the server gracefully when user presses ENTER
        server.stop();
    }
}