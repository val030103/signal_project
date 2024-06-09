package data_management;

import com.data_management.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class WebSocketClientTest {

    class TestDataStorage {
        private List<PatientRecord> records = new ArrayList<>();

        public synchronized void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
            PatientRecord record = new PatientRecord(patientId, measurementValue, recordType, timestamp);
            records.add(record);
        }

        public List<PatientRecord> getRecords() {
            return records;
        }
    }

    class TestWebSocketClientImpl extends WebSocketClientImpl {
        private TestDataStorage testDataStorage;

        public TestWebSocketClientImpl(URI serverUri, TestDataStorage testDataStorage) throws URISyntaxException {
            super(serverUri, DataStorage.getInstance()); // Passing the singleton instance
            this.testDataStorage = testDataStorage;
        }

        @Override
        public void onMessage(String message) {
            String[] parts = message.split(",");
            if (parts.length == 4) {
                int patientId = Integer.parseInt(parts[0]);
                long timestamp = Long.parseLong(parts[1]);
                String recordType = parts[2];
                double measurementValue = Double.parseDouble(parts[3]);
                testDataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
        }
    }

    @Test
    public void testOnMessage() throws URISyntaxException {
        TestDataStorage testDataStorage = new TestDataStorage();
        TestWebSocketClientImpl client = new TestWebSocketClientImpl(new URI("ws://localhost:8080"), testDataStorage);

        // Simulate receiving a WebSocket message
        String message = "1,1616161616161,BloodPressure,120.0";
        client.onMessage(message);

        // Verify that the data was added correctly to the storage
        List<PatientRecord> records = testDataStorage.getRecords();
        assertEquals(1, records.size());
        PatientRecord record = records.get(0);
        assertEquals(1, record.getPatientId());
        assertEquals(120.0, record.getMeasurementValue());
        assertEquals("BloodPressure", record.getRecordType());
        assertEquals(1616161616161L, record.getTimestamp());
    }
}