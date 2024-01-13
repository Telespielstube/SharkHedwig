package SessionTest;

import Message.Advertisement;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Message.MessageFlag;
import Misc.Utilities;
import Session.Identification;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IdentificationTest {


    private static SharkPKIComponent sharkPKIComponent;
    private static Identification identification;
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
        identification = new Identification(sharkPKIComponent);
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
        Method random = identification.getClass().getDeclaredMethod("generateRandomNumber");
        random.setAccessible(true);
        byte[] secNumber = (byte[]) random.invoke(identification);
        System.out.println(Arrays.toString(secNumber));
        byte[] secNumber2 = (byte[]) random.invoke(identification);
        byte[] secNumber3 = (byte[]) random.invoke(identification);
        System.out.println(Arrays.toString(secNumber2));
        System.out.println(Arrays.toString(secNumber3));
        assertNotEquals(secNumber, secNumber2);
        assertNotEquals(secNumber2, secNumber3);
        assertNotEquals(secNumber,secNumber3);
    }

    @Test
    public void testIfRandomNumberGetsEncryptedAndDecrypted() throws NoSuchAlgorithmException, ASAPSecurityException, NoSuchPaddingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method random = identification.getClass().getDeclaredMethod("generateRandomNumber");
        random.setAccessible(true);
        byte[] secNumber = (byte[]) random.invoke(identification);
        byte[] encrypted = Utilities.encryptAsymmetric(secNumber, sharkPKIComponent.getASAPKeyStore().getPublicKey());
        byte[] decrypted = ASAPCryptoAlgorithms.decryptAsymmetric(encrypted, sharkPKIComponent.getASAPKeyStore());
        assertEquals(new String(secNumber, StandardCharsets.UTF_8), new String(decrypted, StandardCharsets.UTF_8));
        assertArrayEquals(secNumber, decrypted);
    }

    @Test
    public void createNewChallengeMessage() throws ASAPSecurityException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method secNumber = identification.getClass().getDeclaredMethod("generateRandomNumber");
        secNumber.setAccessible(true);
        byte[] randomSecNumber = (byte[]) secNumber.invoke(identification);
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE, Utilities.createTimestamp(), Utilities.encryptAsymmetric(randomSecNumber, sharkPKIComponent.getPublicKey()));
        assertInstanceOf(Challenge.class, challenge);
    }

    @Test
    public void responseToAdvertisementIsChallengeMessage() {
        Advertisement advertisement = new Advertisement(UUID.randomUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(identification.transferor(advertisement, "Bobby"));
        assertTrue(object.isPresent());
        assertNotNull(object.get());
    }

    @Test
    public void createSecureRandomNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method random = identification.getClass().getDeclaredMethod("generateRandomNumber");
        random.setAccessible(true);
        assertNotNull(random.invoke(identification));
    }

    @Test
    public void testIfChallengeGetsResponseMessage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ASAPSecurityException {
        Method secNumber = identification.getClass().getDeclaredMethod("generateRandomNumber");
        Method handleChallenge = identification.getClass().getDeclaredMethod("handleChallenge", Challenge.class);
        secNumber.setAccessible(true);
        handleChallenge.setAccessible(true);
        byte[] randomSecNumber = (byte[]) secNumber.invoke(identification);
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE, Utilities.createTimestamp(), Utilities.encryptAsymmetric(randomSecNumber, sharkPKIComponent.getPublicKey()));
        Optional<Response> response = (Optional<Response>) handleChallenge.invoke(identification, challenge);
        response.ifPresent(Assertions::assertNotNull);
        assertInstanceOf(Response.class, response.get());
    }
}
