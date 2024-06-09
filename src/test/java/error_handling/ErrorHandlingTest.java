package error_handling;

import com.data_management.WebSocketClientImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.net.URISyntaxException;

public class ErrorHandlingTest {

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