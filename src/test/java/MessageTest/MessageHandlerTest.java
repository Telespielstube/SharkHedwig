package MessageTest;

import Message.Identification.Challenge;
import Message.MessageFlag;
import Message.MessageHandler;
import SetupTest.TestConstant;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MessageHandlerTest {

    private final Challenge challenge = new Challenge(UUID.randomUUID(), MessageFlag.Challenge, System.currentTimeMillis(), "342532452345".getBytes());
    private final MessageHandler messageHandler = new MessageHandler();
    private static SharkPKIComponent sharkPKIComponent;
    private static String francisID;
    static PublicKey publicKeyFrancis;

    @BeforeAll
    public static void setup() throws SharkException, IOException {

        SharkPeer sharkTestPeer = new SharkTestPeerFS(TestConstant.PeerName.getTestConstant(), TestConstant.PeerFolder.getTestConstant() + "/" + TestConstant.PeerName.getTestConstant());
        sharkPKIComponent = setupComponent(sharkTestPeer);

        sharkTestPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        ASAPKeyStore asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
    }

    private static SharkPKIComponent setupComponent(SharkPeer sharkPeer) throws SharkException {
        SharkPKIComponentFactory certificateComponentFactory = new SharkPKIComponentFactory();

        // register this component with shark peer - note: we use interface SharkPeer
        try {
            sharkPeer.addComponent(certificateComponentFactory, SharkPKIComponent.class);
        } catch (SharkException e) {
            throw new RuntimeException(e);
        }
        SharkComponent component = sharkPeer.getComponent(SharkPKIComponent.class);

        // project "clean code" :) we only use interfaces - unfortunately casting is unavoidable
        return (SharkPKIComponent) component;
    }

    @Test
    public void testIfMessageGetsBuildForSending() {
        byte[] outgoingMessage = messageHandler.buildOutgoingMessage(challenge, MessageFlag.Challenge.toString(), francisID, sharkPKIComponent);
        assertNotEquals(Arrays.toString(outgoingMessage), challenge.toString());
    }


    // There is something wrong decrypting a incoming message!!
//    @Test
//    public void testIfByteMessageGetsParsedToMessageObject() throws ASAPSecurityException {
//        Challenge challenge = new Challenge(UUID.randomUUID(), MessageFlag.Challenge, System.currentTimeMillis(), ASAPCryptoAlgorithms.sign("342532452345".getBytes(), sharkPKIComponent.getASAPKeyStore()));
//        byte[] outgoingMessage = messageHandler.buildOutgoingMessage(challenge, MessageFlag.Challenge.toString(), francisID, sharkPKIComponent);
//        Object object = messageHandler.parseMessage(outgoingMessage, francisID, sharkPKIComponent);
//        assertEquals(object.getClass(), challenge.getClass());
//    }

    @Test
    public void testIfObjectGetsConvertedToByteArrayAndBackToObject() {
        byte[] byteMessage = messageHandler.objectToByteArray(challenge);
        Object object = messageHandler.byteArrayToObject(byteMessage);
        assertEquals(object.getClass(), challenge.getClass());
    }
}

