package com.cardio_generator.outputs;

/**
 * Implements an output strategy that prints the data to the console.
 */
public class ConsoleOutputStrategy implements OutputStrategy {
    /**
     * Outputs the patient data to the console.
     *
     * @param patientId the identifier of the patient
     * @param timestamp the time the data was recorded, in milliseconds since epoch
     * @param label the type of data (e.g., "ECG", "BloodPressure")
     * @param data the actual data value
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        System.out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
    }
}