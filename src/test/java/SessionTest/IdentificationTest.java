package SessionTest;

import Message.MessageHandler;
import Misc.Utilities;
import Session.Sessions.Identification;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Before;
import org.junit.Test;
import javax.crypto.NoSuchPaddingException;
import javax.rmi.CORBA.Util;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static Misc.Utilities.*;
import static org.junit.Assert.*;

public class IdentificationTest {

    private final MessageHandler messageHandler = new MessageHandler();
    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;
    private final Identification identTest = new Identification(this.sharkPKIComponent);
    private KeyPair pair;
    public IdentificationTest() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

//    @Before
//    public void setupKeyPair() {
//        KeyPairGenerator generator = null;
//        try {
//            generator = KeyPairGenerator.getInstance("RSA");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        generator.initialize(2048);
//        pair = generator.generateKeyPair();
//    }
//
//    @Test
//    public void testIfRandomNumberGetsEncryptedAndDecrypted() throws NoSuchAlgorithmException {
//        byte[] random = identTest.generateRandomNumber();
//        byte[] encrypted = Utilities.encryptRandomNumber(random, pair.getPublic());
//        byte[] decrypted = ASAPCryptoAlgorithms.decryptAsymmetric(encrypted, (ASAPKeyStore) pair.getPrivate());
//        assertEquals(new String(random, StandardCharsets.UTF_8), new String(decrypted, StandardCharsets.UTF_8));
//        assertArrayEquals(random, decrypted);
//    }
}
