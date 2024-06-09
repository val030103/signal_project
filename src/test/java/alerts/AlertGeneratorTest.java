package alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.alerts.AlertGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AlertGeneratorTest {

    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setUp() {
        dataStorage = DataStorage.getInstance();
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    public void testGenerateBloodPressureTrendAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(130, "BloodPressure", System.currentTimeMillis() - 20000);
        patient.addRecord(145, "BloodPressure", System.currentTimeMillis() - 10000);
        patient.addRecord(160, "BloodPressure", System.currentTimeMillis());

        dataStorage.addPatientData(1, 130, "BloodPressure", System.currentTimeMillis() - 20000);
        dataStorage.addPatientData(1, 145, "BloodPressure", System.currentTimeMillis() - 10000);
        dataStorage.addPatientData(1, 160, "BloodPressure", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        // Check for the alert in the output or log
    }

    @Test
    public void testGenerateCriticalBloodPressureAlert() {
        Patient patient = new Patient(2);
        patient.addRecord(185, "BloodPressure", System.currentTimeMillis());

        dataStorage.addPatientData(2, 185, "BloodPressure", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        // Check for the alert in the output or log
    }

    @Test
    public void testGenerateLowBloodSaturationAlert() {
        Patient patient = new Patient(3);
        patient.addRecord(90, "BloodSaturation", System.currentTimeMillis());

        dataStorage.addPatientData(3, 90, "BloodSaturation", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        // Check for the alert in the output or log
    }

    @Test
    public void testGenerateRapidDropInBloodSaturationAlert() {
        Patient patient = new Patient(4);
        patient.addRecord(95, "BloodSaturation", System.currentTimeMillis() - 500000);
        patient.addRecord(85, "BloodSaturation", System.currentTimeMillis());

        dataStorage.addPatientData(4, 95, "BloodSaturation", System.currentTimeMillis() - 500000);
        dataStorage.addPatientData(4, 85, "BloodSaturation", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        // Check for the alert in the output or log
    }

    @Test
    public void testGenerateHypoxemiaAlert() {
        Patient patient = new Patient(5);
        patient.addRecord(85, "BloodSaturation", System.currentTimeMillis());
        patient.addRecord(80, "BloodPressure", System.currentTimeMillis());

        dataStorage.addPatientData(5, 85, "BloodSaturation", System.currentTimeMillis());
        dataStorage.addPatientData(5, 80, "BloodPressure", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        // Check for the alert in the output or log
    }

    @Test
    public void testNoAlertCondition() {
        Patient patient = new Patient(6);
        patient.addRecord(120, "BloodPressure", System.currentTimeMillis());
        patient.addRecord(96, "BloodSaturation", System.currentTimeMillis());

        dataStorage.addPatientData(6, 120, "BloodPressure", System.currentTimeMillis());
        dataStorage.addPatientData(6, 96, "BloodSaturation", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        // Ensure no alert is triggered
    }

    @Test
    public void testGenerateECGAbnormalAlert() {
        Patient patient = new Patient(7);
        patient.addRecord(0.8, "ECG", System.currentTimeMillis() - 60000);
        patient.addRecord(0.9, "ECG", System.currentTimeMillis() - 50000);
        patient.addRecord(0.85, "ECG", System.currentTimeMillis() - 40000);
        patient.addRecord(1.0, "ECG", System.currentTimeMillis() - 30000);
        patient.addRecord(5.0, "ECG", System.currentTimeMillis() - 20000);  // Peak that should trigger the alert

        dataStorage.addPatientData(7, 0.8, "ECG", System.currentTimeMillis() - 60000);
        dataStorage.addPatientData(7, 0.9, "ECG", System.currentTimeMillis() - 50000);
        dataStorage.addPatientData(7, 0.85, "ECG", System.currentTimeMillis() - 40000);
        dataStorage.addPatientData(7, 1.0, "ECG", System.currentTimeMillis() - 30000);
        dataStorage.addPatientData(7, 5.0, "ECG", System.currentTimeMillis() - 20000);

        alertGenerator.evaluateData(patient);

        // Check for the alert in the output or log
    }
}