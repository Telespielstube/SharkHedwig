package SetupTest;

import DeliveryContract.ShippingLabel;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Message.MessageBuilder;
import Message.MessageFlag;
import Message.MessageHandler;
import Misc.Utilities;
import Session.SessionManager;
import Session.SessionState;
import Session.Sessions.Identification;
import Setup.Channel;
import Setup.ProtocolState;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
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
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class SharkComponentTest {

    private SessionManager sessionManager;
    private Response response;
    private SharkPKIComponent sharkPKIComponent;
    private static ASAPKeyStore asapKeyStore;
    private static String francisID;
    private static PublicKey publicKeyFrancis;
    private static Identification identification;
    private static ShippingLabel shippingLabel;


    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkTestPeerFS testSharkPeer = new SharkTestPeerFS(TestConstant.PeerName.getTestConstant(), TestConstant.PeerFolder.getTestConstant());
        SharkPKIComponent sharkPKIComponent = setupComponent(testSharkPeer);
        testSharkPeer.start();
        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);
        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        identification = new Identification(sharkPKIComponent);
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
        String uri = "sn2://Advertisement";
        assertEquals(uri, Channel.Advertisement.getChannel());
    }

    @Test
    public void handleAnEmptyMessageObjectFromSessionHandler() throws NoSuchFieldException, IllegalAccessException, ASAPSecurityException, NoSuchPaddingException, NoSuchAlgorithmException {
        MessageHandler messageHandler = new MessageHandler();
        SessionManager sessionManager = new SessionManager(SessionState.Identification, ProtocolState.Transferor, null, sharkPKIComponent);
        Field sessionStateField = sessionManager.getClass().getDeclaredField("sessionState");
        Field noSessionField = sessionManager.getClass().getDeclaredField("noSession");
        sessionStateField.setAccessible(true);
        noSessionField.setAccessible(true);
        sessionStateField.set(sessionManager, SessionState.Identification);
        noSessionField.set(sessionManager, true);
        byte[] number = "4634563456".getBytes();
        byte[] encrypted = Utilities.encryptAsymmetric(number, asapKeyStore.getPublicKey());
        Response response = new Response(Utilities.createUUID(), MessageFlag.Response, Utilities.createTimestamp(), encrypted, number);
        Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(response, "Marta");
        if (messageBuilder.isPresent()) {
             assertNotNull(messageBuilder.get());
        } else {
            assertEquals(messageBuilder, Optional.empty());
        }
    }

    @Test
    public void cleanUp() {
        File certificates = Paths.get(String.format("./src/test/resources/TestStorage/ASAPSharkCertificates")).toFile();
        File credentials = Paths.get(String.format("./src/test/resources/TestStorage/SharkCredentials")).toFile();
        certificates.deleteOnExit();
        credentials.deleteOnExit();
    }
}
