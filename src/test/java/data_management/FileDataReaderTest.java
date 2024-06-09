package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;

/**
 * Unit tests for the FileDataReader class.
 */
public class FileDataReaderTest {

    private static final String TEST_DIR = "test_files";
    private DataStorage dataStorage;
    private FileDataReader reader;

    @BeforeEach
    public void setUp() throws IOException {
        // Set up the test directory and create test files
        dataStorage = DataStorage.getInstance();
        reader = new FileDataReader(TEST_DIR);

        Path testDirPath = Paths.get(TEST_DIR);
        if (Files.exists(testDirPath)) {
            Files.walk(testDirPath)
                    .map(Path::toFile)
                    .forEach(file -> file.delete());
            Files.deleteIfExists(testDirPath);
        }
        Files.createDirectory(testDirPath);

        // Create test files
        Files.write(testDirPath.resolve("testfile.txt"), List.of(
                "1,1714376789050,WhiteBloodCells,100.0",
                "1,1714376789051,WhiteBloodCells,200.0"
        ));

        Files.write(testDirPath.resolve("emptyfile.txt"), List.of());

        Files.write(testDirPath.resolve("malformedfile.txt"), List.of(
                "this is a malformed line"
        ));
    }

    @Test
    public void testReadData() throws IOException {
        reader.readData(dataStorage);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size());
        assertEquals(100.0, records.get(0).getMeasurementValue());
        assertEquals(200.0, records.get(1).getMeasurementValue());
    }

    @Test
    public void testReadEmptyFile() throws IOException {
        reader = new FileDataReader(TEST_DIR + "/emptyfile.txt");
        reader.readData(dataStorage);

        List<PatientRecord> records = dataStorage.getRecords(1, 0, System.currentTimeMillis());
        assertTrue(records.isEmpty(), "There should be no records for an empty file");
    }

    @Test
    public void testReadMalformedFile() throws IOException {
        reader = new FileDataReader(TEST_DIR + "/malformedfile.txt");

        assertThrows(IllegalArgumentException.class, () -> {
            reader.readData(dataStorage);
        }, "Malformed file should throw an IllegalArgumentException");
    }

    @Test
    public void testReadFileNotFound() {
        FileDataReader invalidReader = new FileDataReader("nonexistent_directory/nonexistent_file.txt");
        assertThrows(IOException.class, () -> {
            invalidReader.readData(dataStorage);
        });
    }
}