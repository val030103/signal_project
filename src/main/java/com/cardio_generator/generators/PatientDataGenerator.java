package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Represents a data generator for patient-specific health data.
 * This interface defines the contract for classes that will generate
 * and send health data to an output strategy based on a given patient ID.
 */
public interface PatientDataGenerator {

    /**
     * Generates health data for a specified patient and sends it to an output strategy.
     * Implementations of this method should focus on generating realistic health data
     * appropriate to the type of generator (e.g., ECG, blood pressure).
     *
     * @param patientId the unique identifier of the patient for whom data is to be generated
     * @param outputStrategy the output strategy to which the generated data will be sent
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
