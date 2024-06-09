package data_management;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Unit tests for the Patient class.
 */
public class PatientTest {

    @Test
    public void testAddAndRetrieveRecords() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = patient.getRecords(1714376789050L, 1714376789051L);
        assertEquals(2, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
        assertEquals(200.0, records.get(1).getMeasurementValue());
    }

    @Test
    public void testGetRecordsForNonExistingTimeRange() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = patient.getRecords(1714376789000L, 1714376789049L);
        assertTrue(records.isEmpty());
    }

    @Test
    public void testGetPatientId() {
        Patient patient = new Patient(1);
        assertEquals(1, patient.getPatientId());
    }

    @Test
    public void testGetRecordsWithinSpecificRange() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789052L);

        List<PatientRecord> records = patient.getRecords(1714376789050L, 1714376789051L);
        assertEquals(1, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
    }

    @Test
    public void testAddMultipleRecords() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "WhiteBloodCells", 1714376789050L);
        patient.addRecord(150.0, "BloodPressure", 1714376789051L);
        patient.addRecord(200.0, "WhiteBloodCells", 1714376789052L);

        List<PatientRecord> records = patient.getRecords(1714376789050L, 1714376789052L);
        assertEquals(3, records.size());
    }
}