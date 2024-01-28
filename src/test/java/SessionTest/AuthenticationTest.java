package SessionTest;

import Message.Advertisement;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Message.MessageFlag;
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
        assertNotEquals(secNumber,secNumber3);
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
    public void testIfChallengeGetsResponseMessage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ASAPSecurityException, NoSuchFieldException {
        Method secNumber = authentication.getClass().getDeclaredMethod("generateRandomNumber");
        Method handleChallenge = authentication.getClass().getDeclaredMethod("handleChallenge", Challenge.class);
        Field optionalMessage = authentication.getClass().getDeclaredField("optionalMessage");
        secNumber.setAccessible(true);
        handleChallenge.setAccessible(true);
        optionalMessage.setAccessible(true);
        byte[] randomSecNumber = (byte[]) secNumber.invoke(authentication);
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE, Utilities.createTimestamp(), Utilities.encryptAsymmetric(randomSecNumber, sharkPKIComponent.getPublicKey()));
        handleChallenge.invoke(authentication, challenge);
        Optional<Response> response = (Optional<Response>) optionalMessage.get(authentication);
        assertTrue(response.isPresent());
    }
}
