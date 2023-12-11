package Message;

import Message.Identification.Challenge;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.InMemoASAPKeyStore;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import static Misc.Constants.PEER_NAME;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    private final byte[] testMessage = "Hello!!".getBytes();
    private final MessageHandler messageHandler = new MessageHandler();
    private final SecureRandom secure = new SecureRandom();
   // private final Challenge challenge = new Challenge(UUID.randomUUID(),"1", System.currentTimeMillis());

//    @Test
//    public void testIfObjectToByteArrayWorks() {
//        Challenge challenge = new Challenge(UUID.randomUUID(),"1", System.currentTimeMillis());
//        byte[] byteStream = messageHandler.objectToByteArray(challenge);
//        assertTrue(Arrays.toString(messageHandler.objectToByteArray(challenge)), true);
//    }
//
//    @Test
//    public void testIfByteMessageGetsSerialized() {
//        System.out.println(Arrays.toString(messageHandler.serializeMessage(testMessage, "Peter")));
//        assertNotEquals(testMessage, messageHandler.serializeMessage(testMessage, "Peter"));
//    }
//
//    @Test
//    public void testIfObjectIsConvertedToByteArraySerializedAndDeserializedCorrectly() {
//        Challenge challenge = new Challenge(UUID.randomUUID(),"1", System.currentTimeMillis());
//        byte[] byteStream = messageHandler.objectToByteArray(challenge);
//        byte[] serialized = messageHandler.serializeMessage(byteStream, "Peter");
//        byte[] deserialized = messageHandler.deserializeMessage(serialized);
//        Object object = messageHandler.byteArrayToObject(deserialized);
//        assertNotEquals(serialized, byteStream);
//        assertEquals(object.getClass(), Challenge.class);
//    }
//
//    @Test
//    public void testIfMessageSerializedAndDeserializedCorrectly() {
//        byte[] serialized = messageHandler.serializeMessage(testMessage, "Peter");
//        byte[] deserialized = messageHandler.deserializeMessage(serialized);
//        System.out.println(Arrays.toString(serialized).equals(Arrays.toString(deserialized)));
//        assertNotEquals(serialized, deserialized);
//    }
}
