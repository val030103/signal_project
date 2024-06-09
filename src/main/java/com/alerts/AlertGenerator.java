package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class AlertGenerator {
    private DataStorage dataStorage;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public void evaluateAllPatients() {
        List<Patient> patients = dataStorage.getAllPatients();
        for (Patient patient : patients) {
            evaluateData(patient);
        }
    }

    private void evaluateData(Patient patient) {
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

    private void triggerAlert(Alert alert) {
        alert.notifyAlert();
    }
}