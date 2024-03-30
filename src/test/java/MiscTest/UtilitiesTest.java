package MiscTest;

import Misc.Utilities;
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
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnitTest class for all message relevant tests. The method declarations are very self-explanatory.
 * * Just to make it easier to understand what unit is tested.
 */
public class UtilitiesTest {

    private static SharkPeer sharkPeer;
    private static SharkPKIComponent sharkPKIComponent;
    private static ASAPKeyStore asapKeyStore;
    private static String francisID;
    private static PublicKey publicKeyFrancis;

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {

        SharkTestPeerFS aliceSharkPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant());
        SharkPKIComponent sharkPKIComponent = setupComponent(aliceSharkPeer);

        aliceSharkPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
    }

    private static SharkPKIComponent setupComponent(SharkPeer sharkPeer) throws SharkException, NoSuchPaddingException, NoSuchAlgorithmException {
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
        return (SharkPKIComponent) component;
    }

    @Test
    public void testIfTwoUUIDsDifferFromEachOther() {
        UUID uuid1 = Utilities.createUUID();
        UUID uuid2 = Utilities.createUUID();
        UUID uuid3 = Utilities.createUUID();
        assertNotEquals(uuid1, uuid2);
        assertNotEquals(uuid2, uuid3);
        assertNotEquals(uuid1, uuid3);
    }

    @Test
    public void checkIfUUIDVersionIsNumber4() {
        assertEquals(4, Utilities.createUUID().version());
    }

    @Test
    public void checkUUIDVariant() {
        System.out.println(Utilities.createUUID().variant());
        assertEquals(2, Utilities.createUUID().variant());
    }

    @Test
    public void testIfIDReadableTimestampIsReturned() {
        String timestamp = Utilities.formattedTimestamp();
        System.out.println(timestamp);
        assertNotNull(timestamp);
    }

    @Test
    public void testIfSystemReturnsTimeInMillis() {
        assertEquals(System.currentTimeMillis(), Utilities.createTimestamp());
    }
}