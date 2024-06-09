package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Generates alerts based on patient data stored in the DataStorage.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an AlertGenerator with the specified DataStorage.
     *
     * @param dataStorage the data storage containing patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates data for all patients in the DataStorage and generates alerts as necessary.
     */
    public void evaluateAllPatients() {
        List<Patient> patients = dataStorage.getAllPatients();
        for (Patient patient : patients) {
            evaluateData(patient);
        }
    }

    /**
     * Evaluates data for a specific patient and generates alerts if any conditions are met.
     *
     * @param patient the patient whose data is to be evaluated
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        AlertStrategy bloodPressureStrategy = new BloodPressureStrategy();
        AlertStrategy heartRateStrategy = new HeartRateStrategy();
        AlertStrategy oxygenSaturationStrategy = new OxygenSaturationStrategy();

        boolean bloodPressureAlert = bloodPressureStrategy.checkAlert(records);
        boolean heartRateAlert = heartRateStrategy.checkAlert(records);
        boolean oxygenSaturationAlert = oxygenSaturationStrategy.checkAlert(records);

        AlertFactory bloodPressureFactory = new BloodPressureAlertFactory();
        AlertFactory heartRateFactory = new ECGAlertFactory();  // Assuming heart rate alerts are handled by ECGAlertFactory
        AlertFactory oxygenSaturationFactory = new BloodOxygenAlertFactory();

        if (bloodPressureAlert) {
            Alert alert = bloodPressureFactory.createAlert(String.valueOf(patient.getPatientId()), "BloodPressure Alert", System.currentTimeMillis());
            alert = new RepeatedAlertDecorator(alert, 10); // Repeats every 10 seconds
            triggerAlert(alert);
        }
        if (heartRateAlert) {
            Alert alert = heartRateFactory.createAlert(String.valueOf(patient.getPatientId()), "HeartRate Alert", System.currentTimeMillis());
            alert = new PriorityAlertDecorator(alert, "High");
            triggerAlert(alert);
        }
        if (oxygenSaturationAlert) {
            Alert alert = oxygenSaturationFactory.createAlert(String.valueOf(patient.getPatientId()), "OxygenSaturation Alert", System.currentTimeMillis());
            alert = new RepeatedAlertDecorator(alert, 15); // Repeats every 15 seconds
            alert = new PriorityAlertDecorator(alert, "Critical");
            triggerAlert(alert);
        }
    }

    /**
     * Triggers the specified alert.
     *
     * @param alert the alert to be triggered
     */
    private void triggerAlert(Alert alert) {
        alert.notifyAlert();
    }

    /**
     * Main method to demonstrate alert generation with sample patient data.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        DataStorage dataStorage = DataStorage.getInstance();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        // Adding sample patient data
        int patientId1 = 1;
        dataStorage.addPatientData(patientId1, 130, "BloodPressure", System.currentTimeMillis() - 20000);
        dataStorage.addPatientData(patientId1, 145, "BloodPressure", System.currentTimeMillis() - 10000);
        dataStorage.addPatientData(patientId1, 160, "BloodPressure", System.currentTimeMillis());

        int patientId2 = 2;
        dataStorage.addPatientData(patientId2, 185, "BloodPressure", System.currentTimeMillis());

        int patientId3 = 3;
        dataStorage.addPatientData(patientId3, 90, "BloodSaturation", System.currentTimeMillis());

        int patientId4 = 4;
        dataStorage.addPatientData(patientId4, 95, "BloodSaturation", System.currentTimeMillis() - 500000);
        dataStorage.addPatientData(patientId4, 85, "BloodSaturation", System.currentTimeMillis());

        int patientId5 = 5;
        dataStorage.addPatientData(patientId5, 85, "BloodSaturation", System.currentTimeMillis());
        dataStorage.addPatientData(patientId5, 80, "BloodPressure", System.currentTimeMillis());

        int patientId7 = 7;
        dataStorage.addPatientData(patientId7, 0.8, "ECG", System.currentTimeMillis() - 60000);
        dataStorage.addPatientData(patientId7, 0.9, "ECG", System.currentTimeMillis() - 50000);
        dataStorage.addPatientData(patientId7, 0.85, "ECG", System.currentTimeMillis() - 40000);
        dataStorage.addPatientData(patientId7, 1.0, "ECG", System.currentTimeMillis() - 30000);
        dataStorage.addPatientData(patientId7, 5.0, "ECG", System.currentTimeMillis() - 20000);  // Peak that should trigger the alert

        // Evaluate all patients
        alertGenerator.evaluateAllPatients();
    }
}