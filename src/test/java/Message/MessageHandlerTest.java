package Message;

import Channel.Channel;
import Message.Identification.Challenge;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.InMemoASAPKeyStore;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MessageHandlerTest {

    private final byte[] testMessage = "Hello!!".getBytes();
    ASAPPeer asapPeer;
    private InMemoASAPKeyStore aliceKeyStorage;
    private InMemoASAPKeyStore bobKeyStorage;
    private final String ALICE_ID = "ALICE_44";
    private final String BOB_ID = "BOB_12";
    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());
    private MessageHandler messageHandler = new MessageHandler();
    private SharkPKIComponent sharkPKIComponent;

    @Before
    public void setupASAPKeyStores() throws ASAPSecurityException {
        this.aliceKeyStorage = new InMemoASAPKeyStore(ALICE_ID);
        this.bobKeyStorage = new InMemoASAPKeyStore(BOB_ID);
        this.sharkPKIComponent.getASAPKeyStore();


        // simulate key exchange
        this.aliceKeyStorage.addKeyPair(BOB_ID, bobKeyStorage.getKeyPair());
        this.bobKeyStorage.addKeyPair(ALICE_ID, aliceKeyStorage.getKeyPair());
    }

    @Test
    public void testIfObjectGetsConvertedToByteArrayAndBack() {
        byte[] byteMessage = messageHandler.objectToByteArray(challenge);
        Object object = messageHandler.byteArrayToObject(byteMessage);
        assertEquals(object.getClass(), challenge.getClass());
    }

    @Test
    public void testIfObjectGetsBuild() {
        messageHandler.buildOutgoingMessage(challenge, Channel.Identification, "Peter", sharkPKIComponent);
    }
}
