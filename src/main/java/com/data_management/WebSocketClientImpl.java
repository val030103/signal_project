package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClientImpl extends WebSocketClient {
    private DataStorage dataStorage;

    public WebSocketClientImpl(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket connection opened");
    }

    @Override
    public void onMessage(String message) {
        // Parse the incoming message and store the data in DataStorage
        String[] parts = message.split(",");
        if (parts.length == 4) {
            try {
                int patientId = Integer.parseInt(parts[0]);
                long timestamp = Long.parseLong(parts[1]);
                String recordType = parts[2];
                double measurementValue = Double.parseDouble(parts[3]);

                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Malformed data message: " + message, e);
            }
        } else {
            throw new IllegalArgumentException("Malformed data message: " + message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}