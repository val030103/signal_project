package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class fileOutputStrategy implements OutputStrategy {

    // Variable to store the directory where output files will be created.
    // Changed variable name to camelCase
    private String baseDirectory;

    // A map to store file paths for each label, ensuring unique file per label.
    // Changed map to UPPER_SNAKE_CASE
    public final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>();

    /**
     * Constructor to set the base directory for file outputs.
     * 
     * @param baseDirectory the directory to save output files
     */
    public fileOutputStrategy(String baseDirectory) {

        // Initialize the base directory.
        // Changed variable name to camelCase
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs data into a file designated by the label parameter.
     * Creates the file if it does not exist, and appends new data.
     * 
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label the label indicating the type of data
     * @param data the data string to be written
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Ensure directory exists before writing files.
            // Changed variable name to camelCase
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        
        // Define or get the path for the file associated with this label.
        // Changed baseDirectory name to camelCase
        // Changed filePath name to camelCase
        // Changed map to UPPER_SNAKE_CASE
        String filePath = FILE_MAP.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Try with resources to automatically close PrintWriter.
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) { // Changed variable name to camelCase
            // Format and write data to the file.
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage()); // Changed variable name to camelCase
        }
    }
}
