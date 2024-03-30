package SetupTest;

import DeliveryContract.ShippingLabel;
import Message.Advertisement;
import Message.MessageFlag;
import Misc.Utilities;
import Session.ReceivedMessageList;
import Session.SessionManager;
import Session.SessionState;
import Setup.Channel;
import Setup.ProtocolState;
import Setup.SharkHedwigComponent;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Optional;

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
    private static SharkTestPeerFS sharkTestPeerFS;


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
    public void testIfChannelAdvertisementEqualsReceivedURI() {
        String uri = "sn2://advertisement";
        assertEquals(uri, Channel.ADVERTISEMENT.getChannel());
    }

    @Test
    public void testIfACompleteProtcolRunWorks() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, ASAPSecurityException {
        receivedMessageList = new ReceivedMessageList();
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        sessionManager = new SessionManager(SessionState.NO_SESSION, ProtocolState.TRANSFEREE, receivedMessageList, sharkPKIComponent);
        SharkHedwigComponent sharkHedwigComponent = new SharkHedwigComponent(sharkPKIComponent);
        ASAPMessages messages = {advertisement, };
        sharkHedwigComponent.processMessages(messages, "Marta");

        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));

    }

    @Test
    public void cleanUp() {
        File certificates = Paths.get("./src/test/resources/TestStorage/ASAPSharkCertificates").toFile();
        File credentials = Paths.get("./src/test/resources/TestStorage/SharkCredentials").toFile();
        certificates.deleteOnExit();
        credentials.deleteOnExit();
    }
}
