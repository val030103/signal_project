package com.data_management;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileDataReader implements DataReader {
    private String outputDir;

    public FileDataReader(String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        Path dirPath = Paths.get(outputDir);
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            throw new IOException("Directory does not exist or is not a directory: " + outputDir);
        }

        Files.list(dirPath)
            .filter(Files::isRegularFile)
            .forEach(file -> {
                try {
                    List<String> lines = Files.readAllLines(file);
                    for (String line : lines) {
                        parseLine(line, dataStorage);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file);
                    e.printStackTrace();
                }
            });
    }

    private void parseLine(String line, DataStorage dataStorage) {
        String[] parts = line.split(", ");
        if (parts.length < 4) {
            System.err.println("Skipping malformed line: " + line);
            return;
        }
        try {
            int patientId = Integer.parseInt(parts[0].split(": ")[1]);
            long timestamp = Long.parseLong(parts[1].split(": ")[1]);
            String label = parts[2].split(": ")[1];
            double data = Double.parseDouble(parts[3].split(": ")[1]);

            dataStorage.addPatientData(patientId, data, label, timestamp);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
        }
    }
}
