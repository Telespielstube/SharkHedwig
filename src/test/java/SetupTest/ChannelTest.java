package SetupTest;

import Setup.Channel;
import org.junit.Test;

import static org.junit.Assert.*;

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
