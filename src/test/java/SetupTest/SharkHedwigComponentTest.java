package SetupTest;

import DeliveryContract.ShippingLabel;
import Session.ReceivedMessageList;
import Session.SessionManager;
import Setup.Channel;
import Setup.SharkHedwigComponent;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import static org.junit.jupiter.api.Assertions.*;

public class SharkHedwigComponentTest {


    private SessionManager sessionManager;
    private SharkPKIComponent sharkPKIComponent;
    private static SharkTestPeerFS sharkTestPeerFS;
    private static ASAPKeyStore asapKeyStore;
    private static String francisID;
    private static PublicKey publicKeyFrancis;
    private static ShippingLabel shippingLabel;
    private static ReceivedMessageList receivedMessageList;

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        sharkTestPeerFS = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant());
        SharkPKIComponent sharkPKIComponent = setupComponent(sharkTestPeerFS);
        sharkTestPeerFS.start();
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
    public void checkIfSetupHedwigComponentWorksProperly() throws NoSuchFieldException, NoSuchPaddingException, NoSuchAlgorithmException, IOException, SharkException {
        SharkHedwigComponent sharkHedwig = new SharkHedwigComponent();
    }

    @Test
    // Public keys are not equal
    public void comparePublicKeyPairs() throws SharkException, NoSuchPaddingException, IOException, NoSuchAlgorithmException {
        PublicKey pubKey = asapKeyStore.getPublicKey();
        setup();
        assertEquals(asapKeyStore.getPublicKey(), pubKey);
    }

    @Test
    public void testIfChannelAdvertisementEqualsReceivedURI() {
        String uri = "sn2://no_session";
        assertEquals(uri, Channel.NO_SESSION.getChannel());
    }

    @Test
    public void cleanUp() {
        File certificates = Paths.get("./src/test/resources/TestStorage/ASAPSharkCertificates").toFile();
        File credentials = Paths.get("./src/test/resources/TestStorage/SharkCredentials").toFile();
        certificates.deleteOnExit();
        credentials.deleteOnExit();
    }
}
