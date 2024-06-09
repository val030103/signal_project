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

        boolean bloodPressureTrendAlert = checkBloodPressureTrend(records);
        boolean bloodPressureCriticalAlert = checkBloodPressureCritical(records);
        boolean bloodSaturationLowAlert = checkBloodSaturationLow(records);
        boolean bloodSaturationRapidDropAlert = checkBloodSaturationRapidDrop(records);
        boolean hypoxemiaAlert = checkHypoxemia(records);
        boolean ecgAlert = checkECGAnomalies(records);

        AlertFactory bloodPressureFactory = new BloodPressureAlertFactory();
        AlertFactory bloodOxygenFactory = new BloodOxygenAlertFactory();
        AlertFactory ecgFactory = new ECGAlertFactory();

        if (bloodPressureTrendAlert) {
            triggerAlert(bloodPressureFactory.createAlert(String.valueOf(patient.getPatientId()), "BloodPressure Trend", System.currentTimeMillis()));
        }
        if (bloodPressureCriticalAlert) {
            triggerAlert(bloodPressureFactory.createAlert(String.valueOf(patient.getPatientId()), "Critical BloodPressure", System.currentTimeMillis()));
        }
        if (bloodSaturationLowAlert) {
            triggerAlert(bloodOxygenFactory.createAlert(String.valueOf(patient.getPatientId()), "Low BloodSaturation", System.currentTimeMillis()));
        }
        if (bloodSaturationRapidDropAlert) {
            triggerAlert(bloodOxygenFactory.createAlert(String.valueOf(patient.getPatientId()), "Rapid Drop in BloodSaturation", System.currentTimeMillis()));
        }
        if (hypoxemiaAlert) {
            triggerAlert(bloodOxygenFactory.createAlert(String.valueOf(patient.getPatientId()), "Hypoxemia", System.currentTimeMillis()));
        }
        if (ecgAlert) {
            triggerAlert(ecgFactory.createAlert(String.valueOf(patient.getPatientId()), "ECG Anomaly", System.currentTimeMillis()));
        }
    }

    private boolean checkBloodPressureTrend(List<PatientRecord> records) {
        int consecutiveCount = 0;
        double previousValue = 0;
        boolean increasingTrend = false;
        boolean decreasingTrend = false;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodPressure")) {
                double currentValue = record.getMeasurementValue();
                if (consecutiveCount == 0) {
                    previousValue = currentValue;
                    consecutiveCount++;
                } else {
                    double difference = currentValue - previousValue;
                    if (difference > 10) {
                        if (increasingTrend || consecutiveCount == 1) {
                            increasingTrend = true;
                            consecutiveCount++;
                        } else {
                            increasingTrend = false;
                            consecutiveCount = 1;
                        }
                    } else if (difference < -10) {
                        if (decreasingTrend || consecutiveCount == 1) {
                            decreasingTrend = true;
                            consecutiveCount++;
                        } else {
                            decreasingTrend = false;
                            consecutiveCount = 1;
                        }
                    } else {
                        increasingTrend = false;
                        decreasingTrend = false;
                        consecutiveCount = 1;
                    }
                    previousValue = currentValue;
                }

                if (consecutiveCount >= 3) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBloodPressureCritical(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodPressure")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue > 180 || currentValue < 90) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBloodSaturationLow(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodSaturation")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue < 92) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBloodSaturationRapidDrop(List<PatientRecord> records) {
        double previousValue = 0;
        long previousTimestamp = 0;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodSaturation")) {
                double currentValue = record.getMeasurementValue();
                long currentTimestamp = record.getTimestamp();

                if (previousTimestamp != 0 && (currentTimestamp - previousTimestamp) <= 600000) {
                    if ((previousValue - currentValue) >= 5) {
                        return true;
                    }
                }

                previousValue = currentValue;
                previousTimestamp = currentTimestamp;
            }
        }
        return false;
    }

    private boolean checkHypoxemia(List<PatientRecord> records) {
        boolean lowBloodPressure = false;
        boolean lowBloodSaturation = false;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("BloodPressure")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue < 90) {
                    lowBloodPressure = true;
                }
            }
            if (record.getRecordType().equals("BloodSaturation")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue < 92) {
                    lowBloodSaturation = true;
                }
            }
        }

        return lowBloodPressure && lowBloodSaturation;
    }

    private boolean checkECGAnomalies(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG")) {
                double currentValue = record.getMeasurementValue();
                // Define ECG anomaly criteria here
                if (currentValue > 100 || currentValue < 60) { // Example threshold values
                    return true;
                }
            }
        }
        return false;
    }

    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        System.out.println("Alert triggered: " + alert);
    }
}