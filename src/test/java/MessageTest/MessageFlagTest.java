package MessageTest;

import Message.MessageFlag;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MessageFlagTest {

    @Test
    public void testIfMessageFlagIsCorrect() {
        assertEquals(1, MessageFlag.CHALLENGE.getFlag());
    }

    @Test
    public void wrongFlagGetsRejected() {
        assertNotEquals(0, MessageFlag.CONFIRM.getFlag());
    }
}
