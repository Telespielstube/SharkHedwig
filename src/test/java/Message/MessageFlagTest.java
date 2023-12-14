package Message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageFlagTest {

    @Test
    public void testIfMessageFlagIsCorrect() {
        assertEquals(1, MessageFlag.Challenge.getFlag());
    }
}
