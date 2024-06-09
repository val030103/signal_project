package data_management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

class DataStorageTest {

    private DataStorage storage;

    @BeforeEach
    void setUp() {
        storage = new DataStorage();
    }

    @Test
    void testAddAndGetRecords() {
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        assertEquals(200.0, records.get(1).getMeasurementValue()); // Validate second record
    }

    @Test
    void testGetRecordsForNonExistingPatient() {
        List<PatientRecord> records = storage.getRecords(999, 1714376789050L, 1714376789051L);
        assertTrue(records.isEmpty()); // Expecting no records for a non-existing patient
    }

    @Test
    void testGetRecordsOutsideTimeRange() {
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789000L, 1714376789049L);
        assertTrue(records.isEmpty()); // No records should fall within this time range
    }

    @Test
    void testGetAllPatients() {
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(2, 150.0, "BloodPressure", 1714376789051L);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(2, patients.size()); // Check if two patients are retrieved
    }

    @Test
    void testAddDataToExistingPatient() {
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 110.0, "BloodPressure", 1714376789052L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789052L);
        assertEquals(2, records.size()); // Check if two records are retrieved
    }
}