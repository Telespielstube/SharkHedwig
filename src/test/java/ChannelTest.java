import Setup.Channel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChannelTest {

    @Test
    public void testIfCorrectChannelIsResturned() {
        System.out.println(Channel.Advertisement.getChannelType());
        assertEquals("sn2://Advertisement", Channel.Advertisement.getChannelType());
    }
}
