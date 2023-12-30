package MessageTest;

import Message.MessageFlag;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageFlagTest {

    @Test
    public void testIfMessageFlagIsCorrect() {
        assertEquals(1, MessageFlag.Challenge.getFlag());
    }
}
