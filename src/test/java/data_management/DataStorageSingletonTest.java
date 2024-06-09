package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.data_management.*;

/**
 * Unit tests for the DataStorage singleton implementation.
 */
public class DataStorageSingletonTest {

    @Test
    public void testSingletonInstance() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertSame(instance1, instance2, "Both instances should be the same");
    }

    @Test
    public void testAddAndGetRecords() {
        DataStorage dataStorage = DataStorage.getInstance();
        dataStorage.addPatientData(1, 120.0, "BloodPressure", System.currentTimeMillis());

        assertFalse(dataStorage.getRecords(1, 0, System.currentTimeMillis()).isEmpty());
    }
}