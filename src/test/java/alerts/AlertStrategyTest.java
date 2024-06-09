package alerts;

import com.data_management.PatientRecord;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.alerts.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the various AlertStrategy implementations.
 */
public class AlertStrategyTest {

    @Test
    public void testBloodPressureStrategy() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 130, "BloodPressure", System.currentTimeMillis() - 20000));
        records.add(new PatientRecord(1, 145, "BloodPressure", System.currentTimeMillis() - 10000));
        records.add(new PatientRecord(1, 160, "BloodPressure", System.currentTimeMillis()));

        AlertStrategy strategy = new BloodPressureStrategy();
        assertTrue(strategy.checkAlert(records));
    }

    @Test
    public void testHeartRateStrategy() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 50, "HeartRate", System.currentTimeMillis()));

        AlertStrategy strategy = new HeartRateStrategy();
        assertTrue(strategy.checkAlert(records));
    }

    @Test
    public void testOxygenSaturationStrategy() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 90, "BloodSaturation", System.currentTimeMillis()));

        AlertStrategy strategy = new OxygenSaturationStrategy();
        assertTrue(strategy.checkAlert(records));
    }
}