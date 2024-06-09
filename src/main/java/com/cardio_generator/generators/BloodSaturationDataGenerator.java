package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates blood saturation data for patients.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random(); // Random generator for simulating data.
    private int[] lastSaturationValues; // Array to hold the last recorded saturation values for each patient.

    /**
     * Constructor initializes saturation values for a specified number of patients.
     * 
     * @param patientCount the number of patients to generate data for
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1]; // Plus one because patient IDs start from 1.

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100.
        }
    }

    /**
     * Generates and outputs new blood saturation data for a specific patient.
     * Data is adjusted by a small random variation and clamped within healthy limits.
     *
     * @param patientId the identifier of the patient for whom to generate data
     * @param outputStrategy the output strategy to handle the formatted data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations.
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}