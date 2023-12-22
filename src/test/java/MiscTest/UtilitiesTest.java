package MiscTest;

import Message.Identification.Response;
import Misc.Utilities;
import Session.SessionManager;
import Session.SessionState;
import Setup.DeviceState;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.Before;
import org.junit.Test;

import javax.rmi.CORBA.Util;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * JUnitTest class for all message relevant tests. The method declarations are very self-explanatory.
 * * Just to make it easier to understand what unit is tested.
 */
public class UtilitiesTest {

    private SharkPeer sharkPeer;
    private SharkPKIComponent sharkPKIComponent;
    private ASAPKeyStore asapKeyStore;

    @Before
    public void setup() throws SharkException {
        try {
            SharkTestPeerFS sharkPeer = new SharkTestPeerFS("Alice", "tester123/Alice");
            //  sharkPKIComponent = setupComponent(aliceSharkPeer);
            SharkPKIComponentFactory certificateComponentFactory = new SharkPKIComponentFactory();

            // register this component with shark peer - note: we use interface SharkPeer
            try {
                sharkPeer.addComponent(certificateComponentFactory, SharkPKIComponent.class);
            } catch (SharkException e) {
                throw new RuntimeException(e);
            }

            // get component instance
            SharkComponent component = sharkPeer.getComponent(SharkPKIComponent.class);

            // project "clean code" :) we only use interfaces - unfortunately casting is unavoidable
            sharkPKIComponent = (SharkPKIComponent) component;

            sharkPeer.start();

            String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

            asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIfIDReadableTimestampIsReturned() {
        String timestamp = Utilities.createReadableTimestamp();
        System.out.println(timestamp);
        assertNotNull(timestamp);
    }

    @Test
    public void testIfSystemReturnsTimeInMillis() {
        assertEquals(System.currentTimeMillis(), Utilities.createTimestamp(), 0.1);
    }

    @Test
    public void testIfRandomNumberGetsEncryptedAndDecrypted() throws ASAPSecurityException {
        byte[] encrypted = Utilities.encryptRandomNumber("3452345345".getBytes(), sharkPKIComponent.getPublicKey());
        byte[] decrypted = Utilities.decryptRandomNumber(encrypted, sharkPKIComponent.getPrivateKey());
        assertArrayEquals("3452345345".getBytes(), decrypted);
    }
}