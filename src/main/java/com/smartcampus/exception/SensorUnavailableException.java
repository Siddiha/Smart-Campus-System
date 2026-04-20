package com.smartcampus.exception;

/**
 * Exception thrown when an attempt is made to access or interact with a sensor
 * that is currently unavailable. This typically occurs when a sensor is:
 * <ul>
 *   <li>In MAINTENANCE mode</li>
 *   <li>In OFFLINE status</li>
 *   <li>Not responding to requests</li>
 *   <li>Disconnected from the network</li>
 * </ul>
 * 
 * This is a runtime exception (unchecked), meaning it does not require explicit
 * try-catch handling, though it may be caught if recovery is possible.
 */
public class SensorUnavailableException extends RuntimeException {
    
    /**
     * Constructs a new SensorUnavailableException with the specified detail message.
     * 
     * @param message A descriptive message explaining why the sensor is unavailable.
     *                This message can be used for logging or user feedback.
     *                Example: "Sensor with ID SENSOR_001 is currently in MAINTENANCE mode"
     */
    public SensorUnavailableException(String message) {
        super(message);  // Pass the message to the parent RuntimeException class
    }
}