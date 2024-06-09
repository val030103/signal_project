package com.data_management;

import java.io.IOException;

/**
 * Interface for reading data into a DataStorage instance.
 * It includes methods for reading data from a source, connecting to a WebSocket server,
 * and disconnecting from the WebSocket server.
 */
public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Connects to a WebSocket server to receive real-time data.
     *
     * @param url the WebSocket server URL
     * @throws IOException if there is an error connecting to the WebSocket server
     */
    void connectWebSocket(String url) throws IOException;

    /**
     * Disconnects from the WebSocket server.
     *
     * @throws IOException if there is an error disconnecting from the WebSocket server
     */
    void disconnectWebSocket() throws IOException;
}