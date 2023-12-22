package SessionTest;

import Message.MessageHandler;
import Session.Sessions.Identification;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;


import static Misc.Utilities.decryptRandomNumber;
import static Misc.Utilities.encryptRandomNumber;
import static org.junit.Assert.*;

public class IdentificationTest {

    private final MessageHandler messageHandler = new MessageHandler();
    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;
    private final Identification identTest = new Identification(this.sharkPKIComponent);
    private KeyPair pair;
    public IdentificationTest() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    @Before
    public void setupKeyPair() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        generator.initialize(2048);
        pair = generator.generateKeyPair();
    }
    @Test
    public void testIfSecureRandomNumberIsReturnedAsString() {
        System.out.println(identTest.generateRandomNumber());
        assertNotNull(identTest.generateRandomNumber());
    }

    @Test
    public void testIfRandomNumberGetsEncryptedAndDecrypted() throws NoSuchAlgorithmException {
        byte[] random = identTest.generateRandomNumber();
        byte[] encrypted = encryptRandomNumber(random, pair.getPublic());
        byte[] decrypted = decryptRandomNumber(encrypted, pair.getPrivate());
        assertEquals(new String(random, StandardCharsets.UTF_8), new String(decrypted, StandardCharsets.UTF_8));
        assertArrayEquals(random, decrypted);
    }
}
