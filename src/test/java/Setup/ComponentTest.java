package Setup;

import Message.Identification.*;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class ComponentTest {

    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());
    private final Response response = new Response(UUID.randomUUID(), System.currentTimeMillis());
    @Test
    public void returnTrueIfChallengeMessageIsOfTypeIdentification() {
        assertTrue(challenge instanceof Identification);
    }

    @Test
    public void returnTrueIfResponseMessageIsOfTypeIdentification() {
        assertTrue(response instanceof Identification);
    }
}
