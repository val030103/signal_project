package alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.alerts.*;

/**
 * Unit tests for the AlertFactory and its subclasses.
 */
public class AlertFactoryTest {

    @Test
    public void testCreateBloodPressureAlert() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "BloodPressure Alert", System.currentTimeMillis());

        assertEquals("1", alert.getPatientId());
        assertEquals("BloodPressure Alert", alert.getCondition());
        assertTrue(alert.getTimestamp() > 0);
    }

    @Test
    public void testCreateBloodOxygenAlert() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("2", "BloodOxygen Alert", System.currentTimeMillis());

        assertEquals("2", alert.getPatientId());
        assertEquals("BloodOxygen Alert", alert.getCondition());
        assertTrue(alert.getTimestamp() > 0);
    }

    @Test
    public void testCreateECGAlert() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("3", "ECG Alert", System.currentTimeMillis());

        assertEquals("3", alert.getPatientId());
        assertEquals("ECG Alert", alert.getCondition());
        assertTrue(alert.getTimestamp() > 0);
    }
}