package alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.alerts.*;

/**
 * Unit tests for the AlertDecorator and its subclasses.
 */
public class AlertDecoratorTest {

    @Test
    public void testRepeatedAlertDecorator() {
        Alert alert = new Alert("1", "BloodPressure Alert", System.currentTimeMillis());
        Alert repeatedAlert = new RepeatedAlertDecorator(alert, 10);

        // Normally, you would use mocks to test repeated alerts, but we'll just call notifyAlert for simplicity
        repeatedAlert.notifyAlert();

        assertEquals("1", repeatedAlert.getPatientId());
        assertEquals("BloodPressure Alert", repeatedAlert.getCondition());
        assertTrue(repeatedAlert.getTimestamp() > 0);
    }

    @Test
    public void testPriorityAlertDecorator() {
        Alert alert = new Alert("2", "BloodOxygen Alert", System.currentTimeMillis());
        Alert priorityAlert = new PriorityAlertDecorator(alert, "High");

        // Normally, you would use mocks to test alert priority, but we'll just call notifyAlert for simplicity
        priorityAlert.notifyAlert();

        assertEquals("2", priorityAlert.getPatientId());
        assertEquals("BloodOxygen Alert", priorityAlert.getCondition());
        assertTrue(priorityAlert.getTimestamp() > 0);
    }
}