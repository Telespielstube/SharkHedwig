package Setup;

import Message.Identification.*;
import Message.Request.Request;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class ComponentTest {

    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());
    private final Response response = new Response(UUID.randomUUID(), System.currentTimeMillis());
   // private final Request request = new Request();
    @Test
    public void returnTrueIfChallengeMessageIsOfTypeIdentification() {
        assertTrue(challenge instanceof AbstractIdentification);
    }

    @Test
    public void returnTrueIfResponseMessageIsOfTypeIdentification() {
        assertTrue(response instanceof AbstractIdentification);
    }
}
