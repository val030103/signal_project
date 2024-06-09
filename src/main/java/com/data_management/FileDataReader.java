package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class FileDataReader implements DataReader {
    private String filePath;
    private WebSocketClientImpl webSocketClient;

    public FileDataReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int patientId = Integer.parseInt(parts[0]);
                    long timestamp = Long.parseLong(parts[1]);
                    String recordType = parts[2];
                    double measurementValue = Double.parseDouble(parts[3]);

                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                }
            }
        }
    }

    @Override
    public void connectWebSocket(String url) throws IOException {
        try {
            webSocketClient = new WebSocketClientImpl(new URI(url), new DataStorage());
            webSocketClient.connectBlocking();
        } catch (URISyntaxException | InterruptedException e) {
            throw new IOException("WebSocket connection failed", e);
        }
    }

    @Override
    public void disconnectWebSocket() throws IOException {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}