package SessionTest;

import Message.Advertisement;
import Message.Identification.AbstractIdentification;
import Message.Identification.Challenge;
import Message.MessageFlag;
import Misc.Utilities;
import Session.Sessions.Identification;
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
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.SQLOutput;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IdentificationTest {


    private static SharkPKIComponent sharkPKIComponent;
    private static Identification identification;
    private static Challenge challenge;

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {

        SharkPeer sharkTestPeer = new SharkTestPeerFS(TestConstant.PeerName.getTestConstant(), TestConstant.PeerFolder.getTestConstant() + "/" + TestConstant.PeerName.getTestConstant());
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
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.Challenge, Utilities.createTimestamp(), Utilities.encryptAsymmetric(randomSecNumber, sharkPKIComponent.getPublicKey()));
        assertInstanceOf(Challenge.class, challenge);
    }

    @Test
    public void responseToAdvertisementIsChallengeMessage() {
        Advertisement advertisement = new Advertisement(UUID.randomUUID(), MessageFlag.Advertisement, Utilities.createTimestamp(), true);
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
}
