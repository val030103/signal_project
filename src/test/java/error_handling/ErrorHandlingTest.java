package error_handling;

import com.data_management.WebSocketClientImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Test class for handling errors in WebSocketClientImpl.
 */
public class ErrorHandlingTest {

    /**
     * Tests handling of a malformed data message in WebSocketClientImpl.
     *
     * @throws URISyntaxException if the URI is incorrect
     */
    @Test
    public void testMalformedDataMessage() throws URISyntaxException {
        WebSocketClientImpl client = new WebSocketClientImpl(new URI("ws://localhost:8080"), null); // Mock DataStorage for simplicity
        String malformedMessage = "1,1616161616161,BloodPressure";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.onMessage(malformedMessage);
        });

        String expectedMessage = "Malformed data message";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}