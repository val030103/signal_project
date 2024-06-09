package com.data_management;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that manages storage and retrieval of patient data within a healthcare monitoring system.
 */
public class DataStorage {
    private static DataStorage instance;
    private ConcurrentMap<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.

    private DataStorage() {
        this.patientMap = new ConcurrentHashMap<>();
    }

    /**
     * Returns the singleton instance of the DataStorage.
     *
     * @return the singleton instance
     */
    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    /**
     * Adds or updates patient data in the storage.
     * If the patient does not exist, a new Patient object is created and added to the storage.
     * Otherwise, the new data is added to the existing patient's records.
     *
     * @param patientId        the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate", "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in milliseconds since the Unix epoch
     */
    public synchronized void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by a time range.
     *
     * @param patientId the unique identifier of the patient whose records are to be retrieved
     * @param startTime the start of the time range, in milliseconds since the Unix epoch
     * @param endTime   the end of the time range, in milliseconds since the Unix epoch
     * @return a list of PatientRecord objects that fall within the specified time range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }

    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    public static void main(String[] args) {
        DataStorage storage = DataStorage.getInstance();

        // Adding patient data
        storage.addPatientData(1, 120.0, "BloodPressure", System.currentTimeMillis());
        storage.addPatientData(1, 125.0, "BloodPressure", System.currentTimeMillis() + 1000);
        storage.addPatientData(2, 98.0, "BloodSaturation", System.currentTimeMillis());

        // Retrieving and printing records for patient 1
        List<PatientRecord> patient1Records = storage.getRecords(1, 0, System.currentTimeMillis() + 2000);
        System.out.println("Records for patient 1:");
        for (PatientRecord record : patient1Records) {
            System.out.println("Type: " + record.getRecordType() +
                    ", Value: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }

        // Retrieving and printing all patients
        List<Patient> allPatients = storage.getAllPatients();
        System.out.println("All patients:");
        for (Patient patient : allPatients) {
            System.out.println("Patient ID: " + patient.getPatientId());
        }
    }
}