package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;

public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;

    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

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