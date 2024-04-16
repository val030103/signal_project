package com.cardio_generator.outputs;

/**
 * Defines a strategy for outputting patient health data. This interface
 * should be implemented by classes that manage the output of data to different
 * destinations like consoles, files, web sockets, or TCP connections.
 */
public interface OutputStrategy {

    /**
     * Outputs health data for a given patient. Implementations of this method
     * are responsible for formatting and transmitting the data to the appropriate
     * output destination specified by the concrete class (e.g., console, file, web).
     * 
     * @param patientId the unique identifier of the patient
     * @param timestamp the timestamp of the data generation, representing when the data was recorded or generated
     * @param label a label describing the type of data (e.g., "ECG", "Blood Pressure")
     * @param data the actual health data as a string, which could include measurements or alerts
     */
    void output(int patientId, long timestamp, String label, String data);
}
