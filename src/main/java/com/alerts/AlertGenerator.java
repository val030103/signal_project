package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        boolean bloodPressureTrendAlert = checkBloodPressureTrend(records);
        boolean bloodPressureCriticalAlert = checkBloodPressureCritical(records);
        boolean bloodSaturationLowAlert = checkBloodSaturationLow(records);
        boolean bloodSaturationRapidDropAlert = checkBloodSaturationRapidDrop(records);
        boolean hypoxemiaAlert = checkHypoxemia(records);
        boolean ecgAbnormalAlert = checkECGAbnormal(records);

        if (bloodPressureTrendAlert) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "BloodPressure Trend", System.currentTimeMillis()));
        }
        if (bloodPressureCriticalAlert) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical BloodPressure", System.currentTimeMillis()));
        }
        if (bloodSaturationLowAlert) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Low BloodSaturation", System.currentTimeMillis()));
        }
        if (bloodSaturationRapidDropAlert) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Rapid Drop in BloodSaturation", System.currentTimeMillis()));
        }
        if (hypoxemiaAlert) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Hypoxemia", System.currentTimeMillis()));
        }
        if (ecgAbnormalAlert) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Abnormal ECG Data", System.currentTimeMillis()));
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

    private boolean checkECGAbnormal(List<PatientRecord> records) {
        final int WINDOW_SIZE = 5;
        final double THRESHOLD_MULTIPLIER = 2.0;
        
        double sum = 0;
        int count = 0;
        
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            
            if (record.getRecordType().equals("ECG")) {
                sum += record.getMeasurementValue();
                count++;
                
                if (count == WINDOW_SIZE) {
                    double average = sum / WINDOW_SIZE;
                    
                    for (int j = i - WINDOW_SIZE + 1; j <= i; j++) {
                        if (records.get(j).getMeasurementValue() > THRESHOLD_MULTIPLIER * average) {
                            return true;
                        }
                    }
                    
                    sum -= records.get(i - WINDOW_SIZE + 1).getMeasurementValue();
                    count--;
                }
            }
        }
        
        return false;
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        System.out.println("Alert triggered: " + alert);
    }

    /**
     * Main method to run the AlertGenerator.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        DataStorage dataStorage = new DataStorage();

        // Create sample patients and records
        Patient patient1 = new Patient(1);
        patient1.addRecord(130, "BloodPressure", System.currentTimeMillis() - 20000);
        patient1.addRecord(145, "BloodPressure", System.currentTimeMillis() - 10000);
        patient1.addRecord(160, "BloodPressure", System.currentTimeMillis());

        Patient patient2 = new Patient(2);
        patient2.addRecord(185, "BloodPressure", System.currentTimeMillis());

        Patient patient3 = new Patient(3);
        patient3.addRecord(90, "BloodSaturation", System.currentTimeMillis());

        Patient patient4 = new Patient(4);
        patient4.addRecord(95, "BloodSaturation", System.currentTimeMillis() - 500000);
        patient4.addRecord(85, "BloodSaturation", System.currentTimeMillis());

        Patient patient5 = new Patient(5);
        patient5.addRecord(85, "BloodSaturation", System.currentTimeMillis());
        patient5.addRecord(80, "BloodPressure", System.currentTimeMillis());

        Patient patient6 = new Patient(6);
        patient6.addRecord(120,"BloodPressure", System.currentTimeMillis());
        patient6.addRecord(96, "BloodSaturation", System.currentTimeMillis());

        Patient patient7 = new Patient(7);
        patient7.addRecord(0.8, "ECG", System.currentTimeMillis() - 60000);
        patient7.addRecord(0.9, "ECG", System.currentTimeMillis() - 50000);
        patient7.addRecord(0.85, "ECG", System.currentTimeMillis() - 40000);
        patient7.addRecord(1.0, "ECG", System.currentTimeMillis() - 30000);
        patient7.addRecord(5.0, "ECG", System.currentTimeMillis() - 20000); // Peak that should trigger the alert
    
        dataStorage.addPatientData(1, 130, "BloodPressure", System.currentTimeMillis() - 20000);
        dataStorage.addPatientData(1, 145, "BloodPressure", System.currentTimeMillis() - 10000);
        dataStorage.addPatientData(1, 160, "BloodPressure", System.currentTimeMillis());
        dataStorage.addPatientData(2, 185, "BloodPressure", System.currentTimeMillis());
        dataStorage.addPatientData(3, 90, "BloodSaturation", System.currentTimeMillis());
        dataStorage.addPatientData(4, 95, "BloodSaturation", System.currentTimeMillis() - 500000);
        dataStorage.addPatientData(4, 85, "BloodSaturation", System.currentTimeMillis());
        dataStorage.addPatientData(5, 85, "BloodSaturation", System.currentTimeMillis());
        dataStorage.addPatientData(5, 80, "BloodPressure", System.currentTimeMillis());
        dataStorage.addPatientData(6, 120, "BloodPressure", System.currentTimeMillis());
        dataStorage.addPatientData(6, 96, "BloodSaturation", System.currentTimeMillis());
        dataStorage.addPatientData(7, 0.8, "ECG", System.currentTimeMillis() - 60000);
        dataStorage.addPatientData(7, 0.9, "ECG", System.currentTimeMillis() - 50000);
        dataStorage.addPatientData(7, 0.85, "ECG", System.currentTimeMillis() - 40000);
        dataStorage.addPatientData(7, 1.0, "ECG", System.currentTimeMillis() - 30000);
        dataStorage.addPatientData(7, 5.0, "ECG", System.currentTimeMillis() - 20000);
    
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
    
        alertGenerator.evaluateData(patient1);
        alertGenerator.evaluateData(patient2);
        alertGenerator.evaluateData(patient3);
        alertGenerator.evaluateData(patient4);
        alertGenerator.evaluateData(patient5);
        alertGenerator.evaluateData(patient6);
        alertGenerator.evaluateData(patient7);
    }
}