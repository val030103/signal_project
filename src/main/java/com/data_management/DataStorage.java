package com.data_management;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static DataStorage instance;
    private ConcurrentMap<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.

    private DataStorage() {
        this.patientMap = new ConcurrentHashMap<>();
    }

    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public synchronized void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }

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