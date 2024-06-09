package com.data_management;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private ConcurrentMap<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.

    public DataStorage() {
        this.patientMap = new ConcurrentHashMap<>();
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
}