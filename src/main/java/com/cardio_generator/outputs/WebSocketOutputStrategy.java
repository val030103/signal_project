package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;

/**
 * Implements an output strategy that sends the data to clients via WebSocket.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;

    /**
     * Constructor to create a WebSocket server on the specified port.
     *
     * @param port the port on which the WebSocket server will listen for connections
     */
    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }

    /**
     * Outputs the patient data to all connected WebSocket clients.
     *
     * @param patientId the identifier of the patient
     * @param timestamp the time the data was recorded, in milliseconds since epoch
     * @param label the type of data (e.g., "ECG", "BloodPressure")
     * @param data the actual data value
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

    /**
     * Simple WebSocket server to handle client connections and incoming messages.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {

        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            // Ensure incoming data messages are formatted correctly
            // Format: "patientId,timestamp,recordType,measurementValue"
            try {
                String[] parts = message.split(",");
                if (parts.length == 4) {
                    @SuppressWarnings("unused")
                    int patientId = Integer.parseInt(parts[0]);
                    @SuppressWarnings("unused")
                    long timestamp = Long.parseLong(parts[1]);
                    @SuppressWarnings("unused")
                    String recordType = parts[2];
                    @SuppressWarnings("unused")
                    double measurementValue = Double.parseDouble(parts[3]);

                    // Handle the incoming data message appropriately
                } else {
                    throw new IllegalArgumentException("Malformed data message");
                }
            } catch (Exception e) {
                // Handle errors such as corrupted data messages
                e.printStackTrace();
            }
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}