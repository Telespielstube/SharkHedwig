package SetupTest;

import Setup.Channel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChannelTest {

    @Test
    public void testIfCorrectChannelIsResturned() {
        assertTrue(Channel.NO_SESSION.getChannel() instanceof String);
        assertEquals("sn2://no_session", Channel.NO_SESSION.getChannel());
    }

    @Test
    public void testIfChannelIsNotOfTypeChannel() {
        assertNotEquals(Channel.NO_SESSION.getChannel(), Channel.REQUEST.getChannel());
    }
}
