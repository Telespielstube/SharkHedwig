package MessageTest;

import Location.Location;
import Message.MessageFlag;
import Message.MessageHandler;
import Message.Request.Offer;
import SetupTest.TestConstant;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MessageHandlerTest {

    private final MessageHandler messageHandler = new MessageHandler();
    private static SharkPKIComponent sharkPKIComponent;
    private static Offer offer;
    private static String francisID;
    static PublicKey publicKeyFrancis;

    @BeforeAll
    public static void setup() throws SharkException, IOException {

        SharkPeer sharkTestPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant() + "/" + TestConstant.PEER_NAME.getTestConstant());
        sharkPKIComponent = setupComponent(sharkTestPeer);
        sharkTestPeer.start();
        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);
        ASAPKeyStore asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        offer = new Offer(UUID.randomUUID(), MessageFlag.OFFER, System.currentTimeMillis(), 1.0, 10.0, new Location("HTW-Berlin", 52.456931, 13.526444));
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
        byte[] outgoingMessage = messageHandler.buildOutgoingMessage(offer, MessageFlag.OFFER.toString(), francisID, sharkPKIComponent);
        assertNotEquals(Arrays.toString(outgoingMessage), offer.toString());
    }

    @Test
    public void testIfMessageGetsSigned() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] byteMessage = MessageHandler.objectToByteArray(offer);
        Method composeSignature = messageHandler.getClass().getDeclaredMethod("composeSignedMessage", byte[].class, SharkPKIComponent.class);
        composeSignature.setAccessible(true);
        byte[] signedMessage = (byte[]) composeSignature.invoke(messageHandler, byteMessage, sharkPKIComponent);
        System.out.println(Arrays.toString(signedMessage));
        assertNotNull(signedMessage);
    }

    @Test
    public void verifySignedMessage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] byteMessage = MessageHandler.objectToByteArray(offer);
        Method composeSignature = messageHandler.getClass().getDeclaredMethod("composeSignedMessage", byte[].class, SharkPKIComponent.class);
        composeSignature.setAccessible(true);
        byte[] signedMessage = (byte[]) composeSignature.invoke(messageHandler, byteMessage, sharkPKIComponent);
        Method verifyMessage = messageHandler.getClass().getDeclaredMethod("verifySignedMessage", byte[].class, String.class, SharkPKIComponent.class);
        verifyMessage.setAccessible(true);
        byte[] verified = (byte[]) verifyMessage.invoke(messageHandler, signedMessage, francisID, sharkPKIComponent);
        assertNotNull(verified);
    }

    // There is something wrong decrypting a incoming message!!
    @Test
    public void testIfByteMessageGetsParsedToMessageObject() throws ASAPSecurityException {
        byte[] outgoingMessage = messageHandler.buildOutgoingMessage(offer, String.valueOf(MessageFlag.OFFER), francisID, sharkPKIComponent);
        System.out.println(outgoingMessage);
        Object object = messageHandler.parseIncomingMessage(outgoingMessage, francisID, sharkPKIComponent);
        assertEquals(object.getClass(), offer.getClass());
    }

    @Test
    public void testIfObjectGetsConvertedToByteArrayAndBackToObject() {
        byte[] byteMessage = MessageHandler.objectToByteArray(offer);
        Object object = MessageHandler.byteArrayToObject(byteMessage);
        assertEquals(object.getClass(), offer.getClass());
    }
}

