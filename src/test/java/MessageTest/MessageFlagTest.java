package MessageTest;

import Message.MessageFlag;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageFlagTest {

    @Test
    public void testIfMessageFlagIsCorrect() {
        Assert.assertEquals(1, MessageFlag.Challenge.getFlag());
    }
}
