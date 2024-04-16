package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket; // Holds the server socket that listens for incoming client connections.
    private Socket clientSocket; // Socket representing a connection with a client.
    private PrintWriter out; // Used to send data to the connected client.

    /**
     * Initializes a TCP server on the specified port and waits for client connections.
     *
     * @param port the TCP port on which the server should listen
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port); // Create the server socket on the given port.
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread.
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept(); // Accept a client connection.
                    out = new PrintWriter(clientSocket.getOutputStream(), true); // Initialize PrintWriter to send data.
                    System.out.println("Client connected: " + clientSocket.getInetAddress()); // Log the connected client address.
                } catch (IOException e) {
                    e.printStackTrace(); // Print the stack trace in case of an IOException.
                }
            });
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if server socket fails to open.
        }
    }

    /**
     * Sends output to the connected TCP client.
     * Formats the data and sends it over the socket connection if a client is connected.
     *
     * @param patientId the identifier of the patient
     * @param timestamp the timestamp of the data
     * @param label the label describing the type of data
     * @param data the actual data to be sent
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) { // Check if the PrintWriter is initialized.
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data); // Format the message with patient data.
            out.println(message); // Send the formatted message to the client.
        }
    }
}
