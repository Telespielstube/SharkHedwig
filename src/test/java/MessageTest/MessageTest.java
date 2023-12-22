package MessageTest;

import org.junit.Test;
import java.util.UUID;
import static org.junit.Assert.*;

import Message.Identification.Challenge;

/**
 * JUnitTest class for all message relevant tests. The method declarations are very self-explanatory.
 * Just to make it easier to understand what unit is tested.
 */
public class MessageTest {

    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());

    @Test
    public void testIfTwoUUIDsDifferFromEachOther() {
        UUID uuid1 = challenge.createUUID();
        UUID uuid2 = challenge.createUUID();
        assertNotEquals(uuid1, uuid2);
    }
    @Test
    public void checkIfUUIDVersionIsNumber4() {
        assertEquals(4, challenge.createUUID().version());
    }
}
