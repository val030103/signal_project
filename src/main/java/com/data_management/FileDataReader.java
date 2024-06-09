package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class to read patient data from a file or WebSocket connection.
 */
public class FileDataReader implements DataReader {
    private String filePath;
    private WebSocketClientImpl webSocketClient;

    /**
     * Constructs a FileDataReader with the specified file path.
     *
     * @param filePath the path of the file to read data from
     */
    public FileDataReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads data from the file specified by the file path and stores it in the data storage.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
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

    /**
     * Connects to a WebSocket server to receive real-time data.
     *
     * @param url the WebSocket server URL
     * @throws IOException if there is an error connecting to the WebSocket server
     */
    @Override
    public void connectWebSocket(String url) throws IOException {
        try {
            webSocketClient = new WebSocketClientImpl(new URI(url), DataStorage.getInstance());
            webSocketClient.connectBlocking();
        } catch (URISyntaxException | InterruptedException e) {
            throw new IOException("WebSocket connection failed", e);
        }
    }

    /**
     * Disconnects from the WebSocket server.
     *
     * @throws IOException if there is an error disconnecting from the WebSocket server
     */
    @Override
    public void disconnectWebSocket() throws IOException {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}