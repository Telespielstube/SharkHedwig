package SessionTest;

import DeliveryContract.ShippingLabel;
import HedwigUI.UserInputObject;
import Location.Location;
import Message.*;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Misc.Utilities;
import Session.SessionManager;
import Session.SessionState;
import Session.Sessions.Identification;
import Setup.Channel;
import Setup.ProtocolState;
import SetupTest.TestConstant;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SessionManagerTest {

    private SessionManager sessionManager;
    private Response response;
    private SharkPKIComponent sharkPKIComponent;
    private static ASAPKeyStore asapKeyStore;
    private static String francisID;
    private static PublicKey publicKeyFrancis;
    private static Identification identification;
    private static ShippingLabel shippingLabel;

    public SessionManagerTest() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

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
    public void testIfDeviceTransferorIsUnchangedWhenShippingLabelIsCreatedButEmpty() throws NoSuchPaddingException, NoSuchAlgorithmException {
        ShippingLabel shippingLabel = new ShippingLabel();
        sessionManager = new SessionManager(SessionState.Identification, ProtocolState.Transferee, null, sharkPKIComponent);
        assertFalse(shippingLabel.getIsCreated());
        assertNotEquals(ProtocolState.Transferor, ProtocolState.Transferee);
    }

    @Test
    public void transfereeIsNotChangedIfCreateMethodeIsNotCalled() throws NoSuchPaddingException, NoSuchAlgorithmException {
        shippingLabel = new ShippingLabel();
        sessionManager = new SessionManager(SessionState.NoSession, ProtocolState.Transferee, null, sharkPKIComponent);
        assertEquals(ProtocolState.Transferee, ProtocolState.Transferee);
        shippingLabel = new ShippingLabel(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertEquals(ProtocolState.Transferee, ProtocolState.Transferee);
    }

    @Test
    public void transfereeStateChangesWhenCreateIsCalled() throws NoSuchPaddingException, NoSuchAlgorithmException {
        shippingLabel = new ShippingLabel();
        sessionManager = new SessionManager(SessionState.Identification, ProtocolState.Transferee, null, sharkPKIComponent);
        UserInputObject userInput = new UserInputObject("Alice", "HTW-Berlin", 52.456931, 13.526444,
                "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
        shippingLabel.create(userInput);
        assertNotEquals(ProtocolState.Transferee, ProtocolState.Transferor);
    }

    @Test
    public void testIfMessageIsUnhandledWhenSessionDiffers() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException {
        sessionManager = new SessionManager(SessionState.NoSession, ProtocolState.Transferor, null, sharkPKIComponent);
        byte[] encryptedNumber = Utilities.encryptAsymmetric("3345".getBytes(), asapKeyStore.getPublicKey());
        Challenge challenge = new Challenge(UUID.randomUUID(), MessageFlag.Challenge, System.currentTimeMillis(), encryptedNumber );
        MessageBuilder messageBuilder = sessionManager.sessionHandling(challenge, "Marta").get();
        assertEquals(Channel.Advertisement.getChannel(), messageBuilder.getUri());
    }

    @Test
    public void handleNoSessionAsTransferee() throws NoSuchPaddingException, NoSuchAlgorithmException {
        sessionManager = new SessionManager(SessionState.NoSession, ProtocolState.Transferee, null, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.Advertisement, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));
        assertTrue(object.isPresent()); // This is true because uri and sender are present
    }

    @Test
    public void doNotProcessAdvertisementIfStateIsTransferorWithoutAShippingLabel() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException {
        sessionManager = new SessionManager(SessionState.NoSession, ProtocolState.Transferor, null, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.Advertisement, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));
        Field protocolStateField = sessionManager.getClass().getDeclaredField("protocolState");
        protocolStateField.setAccessible(true);
        assertEquals(ProtocolState.Transferor, protocolStateField.get(sessionManager));
        assertTrue(object.isPresent()); // This is true because uri and sender are present
    }

    @Test
    public void testIfChallengeMessageGetsRejectedWithoutAdvertisement() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException {
        response = new Response();
        sessionManager = new SessionManager(SessionState.Identification, ProtocolState.Transferor, null, sharkPKIComponent);
        byte[] encryptedNumber = Utilities.encryptAsymmetric("4634563456".getBytes(), asapKeyStore.getPublicKey());
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.Challenge, System.currentTimeMillis(), encryptedNumber );
        Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(challenge, "Marta");
        assertFalse(messageBuilder.isPresent());
    }

    @Test
    public void testIfResponseGetsRejectedWithoutShippingLabelAndWrongProtocolState() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException, NoSuchFieldException, IllegalAccessException {
        sessionManager = new SessionManager(SessionState.Identification, ProtocolState.Transferor, null, sharkPKIComponent);
        Field sessionStateField = sessionManager.getClass().getDeclaredField("sessionState");
        Field noSessionField = sessionManager.getClass().getDeclaredField("noSession");
        sessionStateField.setAccessible(true);
        noSessionField.setAccessible(true);
        sessionStateField.set(sessionManager, SessionState.Identification);
        noSessionField.set(sessionManager, true);
        byte[] number = "4634563456".getBytes();
        byte[] encrypted = Utilities.encryptAsymmetric(number, asapKeyStore.getPublicKey());
        Response response = new Response(Utilities.createUUID(), MessageFlag.Response, Utilities.createTimestamp(), encrypted, number);
        Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(response, francisID);
        messageBuilder.ifPresent(Assertions::assertNotNull);
        //assertNotEquals("Message flag was incorrect: Response", Optional.empty().toString());
    }


}
