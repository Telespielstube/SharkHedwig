package SetupTest;

import Setup.Channel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChannelTest {

    @Test
    public void testIfCorrectChannelIsResturned() {
        assertTrue(Channel.ADVERTISEMENT.getChannel() instanceof String);
        assertEquals("sn2://advertisement", Channel.ADVERTISEMENT.getChannel());
    }

    @Test
    public void testIfChannelIsNotOfTypeChannel() {
        assertNotEquals(Channel.AUTHENTIFICATION.getChannel(), Channel.AUTHENTIFICATION);
    }
}
