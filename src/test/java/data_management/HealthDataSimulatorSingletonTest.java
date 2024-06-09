package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.cardio_generator.HealthDataSimulator;

/**
 * Unit tests for the HealthDataSimulator singleton.
 */
public class HealthDataSimulatorSingletonTest {

    @Test
    public void testSingletonInstance() {
        HealthDataSimulator instance1 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance2 = HealthDataSimulator.getInstance();

        assertSame(instance1, instance2, "Both instances should be the same");
    }
}