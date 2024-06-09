package integration;

import com.data_management.DataStorage;
import com.data_management.WebSocketClientImpl;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.AlertGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.List;

public class IntegrationTest {
    private DataStorage dataStorage;
    private WebSocketClientImpl webSocketClient;
    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        dataStorage = DataStorage.getInstance();
        webSocketClient = new WebSocketClientImpl(new URI("ws://localhost:8080"), dataStorage);
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    public void testRealTimeDataProcessing() throws IOException, InterruptedException {
        webSocketClient.connectBlocking();  // Connect to the WebSocket server
        // Simulate real-time data reception
        webSocketClient.onMessage("1,1616161616161,BloodPressure,120.0");

        List<Patient> patients = dataStorage.getAllPatients();
        assertFalse(patients.isEmpty(), "Patients list should not be empty");

        Patient patient = patients.get(0);
        alertGenerator.evaluateData(patient);

        // Assertions to ensure data is processed correctly
        List<PatientRecord> records = dataStorage.getRecords(1, 0, System.currentTimeMillis());
        assertEquals(1, records.size());
        assertEquals(120.0, records.get(0).getMeasurementValue());
    }
}