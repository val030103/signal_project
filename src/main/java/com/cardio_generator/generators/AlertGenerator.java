package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alerts based on random probabilities for a set number of patients.
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random(); // Random generator for simulating alert events.
    private boolean[] alertStates; // Keeps track of current alert states for each patient (false = resolved, true = active)

    /**
     * Initializes the alert states for each patient.
     * @param patientCount the number of patients to monitor for alerts
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1]; // Include one extra for 1-based indexing
    }

    /**
     * Generates and outputs alert status for a specific patient.
     * An alert can either be triggered or resolved based on random probabilities.
     *
     * @param patientId the identifier of the patient for whom to generate alert data
     * @param outputStrategy the output strategy to handle the formatted data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) { // Check if there is an existing alert
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve the alert
                    alertStates[patientId] = false; // Resolve the alert
                    // Output the resolved alert status
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert being triggered in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p; // Determine if an alert is triggered

                if (alertTriggered) {
                    alertStates[patientId] = true; // Set the alert state to active
                    // Output the triggered alert status
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace(); // Print the stack trace to help diagnose issues.
        }
    }

    /**
     * Main method to demonstrate alert generation for a set number of patients.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        int patientCount = 10; // Number of patients to simulate
        AlertGenerator alertGenerator = new AlertGenerator(patientCount);
        OutputStrategy consoleOutputStrategy = new ConsoleOutputStrategy();

        // Simulate alert generation for each patient
        for (int patientId = 1; patientId <= patientCount; patientId++) {
            alertGenerator.generate(patientId, consoleOutputStrategy);
        }
    }
}