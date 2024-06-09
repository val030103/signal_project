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

public class FileDataReaderTest {

    private static final String TEST_DIR = "test_files";
    private DataStorage dataStorage;
    private FileDataReader reader;

    @BeforeEach
    public void setUp() throws IOException {
        // Set up the test directory and create test files
        dataStorage = new DataStorage();
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
                "patientId: 1, timestamp: 1714376789050, label: WhiteBloodCells, data: 100.0",
                "patientId: 1, timestamp: 1714376789051, label: WhiteBloodCells, data: 200.0"
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
        reader.readData(dataStorage);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789051L);
        // Assuming emptyfile.txt does not add any records, the records should remain unchanged
        assertEquals(2, records.size());
    }

    @Test
    public void testReadMalformedFile() throws IOException {
        reader.readData(dataStorage);

        // There should be no records added from the malformed file
        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size());
    }

    @Test
    public void testReadFileNotFound() {
        FileDataReader invalidReader = new FileDataReader("nonexistent_directory");
        assertThrows(IOException.class, () -> {
            invalidReader.readData(dataStorage);
        });
    }
}