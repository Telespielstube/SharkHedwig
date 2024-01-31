package SessionTest;

import Message.Advertisement;
import Message.Authentication.Ack;
import Message.Authentication.Challenge;
import Message.Authentication.Response;
import Message.Message;
import Message.MessageFlag;
import Message.Request.Offer;
import Misc.Utilities;
import Session.Authentication;
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
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {


    private static SharkPKIComponent sharkPKIComponent;
    private static Authentication authentication;
    private static Challenge challenge;

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {

        SharkPeer sharkTestPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant() + "/" + TestConstant.PEER_NAME.getTestConstant());
        sharkPKIComponent = setupComponent(sharkTestPeer);

        sharkTestPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        ASAPKeyStore asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        String francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        PublicKey publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        authentication = new Authentication(sharkPKIComponent);
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
    public void testIfRandomNumberGetsGenerated() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method random = authentication.getClass().getDeclaredMethod("generateRandomNumber");
        random.setAccessible(true);
        byte[] secNumber = (byte[]) random.invoke(authentication);
        byte[] secNumber2 = (byte[]) random.invoke(authentication);
        byte[] secNumber3 = (byte[]) random.invoke(authentication);
        assertNotEquals(secNumber, secNumber2);
        assertNotEquals(secNumber2, secNumber3);
        assertNotEquals(secNumber, secNumber3);
    }

    @Test
    public void testIfRandomNumberGetsEncryptedAndDecrypted() throws NoSuchAlgorithmException, ASAPSecurityException, NoSuchPaddingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method random = authentication.getClass().getDeclaredMethod("generateRandomNumber");
        random.setAccessible(true);
        byte[] secNumber = (byte[]) random.invoke(authentication);
        byte[] encrypted = Utilities.encryptAsymmetric(secNumber, sharkPKIComponent.getASAPKeyStore().getPublicKey());
        byte[] decrypted = ASAPCryptoAlgorithms.decryptAsymmetric(encrypted, sharkPKIComponent.getASAPKeyStore());
        assertEquals(new String(secNumber, StandardCharsets.UTF_8), new String(decrypted, StandardCharsets.UTF_8));
        assertArrayEquals(secNumber, decrypted);
    }

    @Test
    public void createNewChallengeMessage() throws ASAPSecurityException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method secNumber = authentication.getClass().getDeclaredMethod("generateRandomNumber");
        secNumber.setAccessible(true);
        byte[] randomSecNumber = (byte[]) secNumber.invoke(authentication);
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE, Utilities.createTimestamp(), Utilities.encryptAsymmetric(randomSecNumber, sharkPKIComponent.getPublicKey()));
        assertInstanceOf(Challenge.class, challenge);
    }

    @Test
    public void responseToAdvertisementIsChallengeMessage() {
        Advertisement advertisement = new Advertisement(UUID.randomUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(authentication.transferor(advertisement, "Bobby"));
        assertTrue(object.isPresent());
        assertNotNull(object.get());
    }

    @Test
    public void createSecureRandomNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method random = authentication.getClass().getDeclaredMethod("generateRandomNumber");
        random.setAccessible(true);
        assertNotNull(random.invoke(authentication));
    }

    @Test
    public void testIfSessionIsComplete() throws NoSuchFieldException, IllegalAccessException {
        assertFalse(authentication.getSessionComplete());
        Field session = authentication.getClass().getSuperclass().getDeclaredField("sessionComplete");
        session.setAccessible(true);
        session.set(authentication, true);
        assertTrue(authentication.getSessionComplete());
    }

    @Test
    public void testIfChallengeReturnsResponse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ASAPSecurityException, NoSuchFieldException {
        Method secNumber = authentication.getClass().getDeclaredMethod("generateRandomNumber");
        Method handleChallenge = authentication.getClass().getDeclaredMethod("handleChallenge", Challenge.class);
        Field optionalMessage = authentication.getClass().getDeclaredField("optionalMessage");
        secNumber.setAccessible(true);
        handleChallenge.setAccessible(true);
        optionalMessage.setAccessible(true);
        byte[] randomSecNumber = (byte[]) secNumber.invoke(authentication);
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE, Utilities.createTimestamp(), Utilities.encryptAsymmetric(randomSecNumber, sharkPKIComponent.getPublicKey()));
        handleChallenge.invoke(authentication, challenge);
        Optional<Response> optionalResponse = (Optional<Response>) optionalMessage.get(authentication);
        assertEquals(optionalResponse.get().getClass(), Response.class);
        assertTrue(optionalResponse.isPresent());
    }

    @Test
    public void testIfResponseReturnsResponseReply() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ASAPSecurityException, NoSuchFieldException {
        Method generateRandomNumberNumber = authentication.getClass().getDeclaredMethod("generateRandomNumber");
        Field optionalMessage = authentication.getClass().getDeclaredField("optionalMessage");
        Field savedRandomNumber = authentication.getClass().getDeclaredField("secureNumber");
        generateRandomNumberNumber.setAccessible(true);
        savedRandomNumber.setAccessible(true);
        optionalMessage.setAccessible(true);
        byte[] randomSecNumber = (byte[]) generateRandomNumberNumber.invoke(authentication);
        savedRandomNumber.set(authentication, "12345".getBytes());
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE, Utilities.createTimestamp(), Utilities.encryptAsymmetric(randomSecNumber, sharkPKIComponent.getPublicKey()));
        authentication.addMessageToList(challenge);
        Response response = new Response(Utilities.createUUID(), MessageFlag.RESPONSE, Utilities.createTimestamp(), Utilities.encryptAsymmetric(randomSecNumber, sharkPKIComponent.getPublicKey()), "12345".getBytes());
        Method handleResponse = authentication.getClass().getDeclaredMethod("handleResponse", Response.class);
        handleResponse.setAccessible(true);
        handleResponse.invoke(authentication, response);
        Optional<Response> optionalResponseReply = (Optional<Response>) optionalMessage.get(authentication);
        assertEquals(optionalResponseReply.get().getClass(), Response.class);
        assertTrue(optionalResponseReply.isPresent());
    }

    @Test
    public void testIfResponseReplyReturnsAck() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ASAPSecurityException, NoSuchFieldException {
        Field optionalMessage = authentication.getClass().getDeclaredField("optionalMessage");
        Field savedRandomNumber = authentication.getClass().getDeclaredField("secureNumber");
        savedRandomNumber.setAccessible(true);
        optionalMessage.setAccessible(true);
        savedRandomNumber.set(authentication, "12345".getBytes());
        Response response = new Response(Utilities.createUUID(), MessageFlag.RESPONSE, Utilities.createTimestamp(), "12345".getBytes());
        authentication.addMessageToList(response);
        Method handleResponseReply = authentication.getClass().getDeclaredMethod("handleResponseReply", Response.class);
        handleResponseReply.setAccessible(true);
        handleResponseReply.invoke(authentication, response);
        Optional<Ack> optionalAck = (Optional<Ack>) optionalMessage.get(authentication);
        assertEquals(optionalAck.get().getClass(), Ack.class);
        assertTrue(optionalAck.isPresent());
    }

    @Test
    public void testIfAckReturnsReadyAndReadyReturnsOffer() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        Field optionalMessage = authentication.getClass().getDeclaredField("optionalMessage");
        optionalMessage.setAccessible(true);
        Ack ack = new Ack(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true);
        authentication.addMessageToList(ack);
        Method handleAck = authentication.getClass().getDeclaredMethod("handleAck", Ack.class);
        handleAck.setAccessible(true);
        handleAck.invoke(authentication, ack);
        Optional<Ack> optionalAck = (Optional<Ack>) optionalMessage.get(authentication);
        assertTrue(optionalAck.isPresent());

        Ack ackReady = new Ack(Utilities.createUUID(), MessageFlag.READY, Utilities.createTimestamp(), true);
        Method handleAckReady = authentication.getClass().getDeclaredMethod("handleAck", Ack.class);
        handleAckReady.setAccessible(true);
        handleAckReady.invoke(authentication, ackReady);
        Optional<Offer> optionalOffer = (Optional<Offer>) optionalMessage.get(authentication);
        assertEquals(optionalOffer.get().getClass(), Offer.class);
        assertTrue(optionalOffer.isPresent());
    }
}
