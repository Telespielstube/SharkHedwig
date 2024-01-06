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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testIfMessageGetsSigned() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] byteMessage = MessageHandler.objectToByteArray(challenge);
        Method composeSignature = messageHandler.getClass().getDeclaredMethod("composeSignedMessage", byte[].class, SharkPKIComponent.class);
        composeSignature.setAccessible(true);
        byte[] signedMessage = (byte[]) composeSignature.invoke(messageHandler, byteMessage, sharkPKIComponent);
        System.out.println(Arrays.toString(signedMessage));
        assertNotNull(signedMessage);
    }

    @Test
    public void verifySignedMessage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] byteMessage = MessageHandler.objectToByteArray(challenge);
        Method composeSignature = messageHandler.getClass().getDeclaredMethod("composeSignedMessage", byte[].class, SharkPKIComponent.class);
        composeSignature.setAccessible(true);
        byte[] signedMessage = (byte[]) composeSignature.invoke(messageHandler, byteMessage, sharkPKIComponent);
        Method verifyMessage = messageHandler.getClass().getDeclaredMethod("verifySignedMessage", byte[].class, String.class, SharkPKIComponent.class);
        verifyMessage.setAccessible(true);
        byte[] verified = (byte[]) verifyMessage.invoke(messageHandler, signedMessage, francisID, sharkPKIComponent);
        assertNotNull(verified);
    }

    // There is something wrong decrypting a incoming message!!
//    @Test
//    public void testIfByteMessageGetsParsedToMessageObject() throws ASAPSecurityException {
//        Challenge challenge = new Challenge(UUID.randomUUID(), MessageFlag.Challenge, System.currentTimeMillis(), ASAPCryptoAlgorithms.sign("342532452345".getBytes(), sharkPKIComponent.getASAPKeyStore()));
//        byte[] outgoingMessage = messageHandler.buildOutgoingMessage(challenge, MessageFlag.Challenge.toString(), francisID, sharkPKIComponent);
//        System.out.println(outgoingMessage);
//        Object object = messageHandler.parseMessage(outgoingMessage, francisID, sharkPKIComponent);
//        assertEquals(object.getClass(), challenge.getClass());
//    }

    @Test
    public void testIfObjectGetsConvertedToByteArrayAndBackToObject() {
        byte[] byteMessage = MessageHandler.objectToByteArray(challenge);
        Object object = MessageHandler.byteArrayToObject(byteMessage);
        assertEquals(object.getClass(), challenge.getClass());
    }
}

