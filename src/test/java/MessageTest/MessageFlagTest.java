package MessageTest;

import Message.Message;
import Message.MessageFlag;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MessageFlagTest {

    @Test
    public void testIfMessageFlagIsCorrect() {
        assertEquals(4, MessageFlag.OFFER.getFlag());
    }

    @Test
    public void wrongFlagGetsRejected() {
        assertNotEquals(0, MessageFlag.CONFIRM.getFlag());
    }

    @Test
    public void testIfEnumIsNotAnDefinedReturnValueIsFalse() {
        EnumSet<MessageFlag> enums = EnumSet.allOf(MessageFlag.class);
        assertFalse(enums.contains(50));

    }
}
