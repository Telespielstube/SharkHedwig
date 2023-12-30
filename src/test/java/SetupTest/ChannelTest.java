package SetupTest;

import Setup.Channel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChannelTest {

    @Test
    public void testIfCorrectChannelIsResturned() {
        assertTrue(Channel.Advertisement.getChannelType() instanceof String);
        assertEquals("sn2://Advertisement", Channel.Advertisement.getChannelType());
    }

    @Test
    public void testIfChannelIsNotOfTypeChannel() {
        assertNotEquals(Channel.Identification.getChannelType(), Channel.Identification);
    }
}
