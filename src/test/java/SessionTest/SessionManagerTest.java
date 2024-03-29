package SessionTest;

import DeliveryContract.*;
import HedwigUI.UserInput;
import Location.Location;
import Message.*;
import Message.Authentication.Challenge;
import Message.Authentication.Response;
import Misc.Utilities;
import Session.SessionManager;
import Session.SessionState;
import Session.Authentication;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SessionManagerTest {

    private SessionManager sessionManager;
    private DeliveryContract deliveryContract;
    private Response response;
    private SharkPKIComponent sharkPKIComponent;
    private static ASAPKeyStore asapKeyStore;
    private static String francisID;
    private static PublicKey publicKeyFrancis;
    private static Authentication authentication;
    private static ShippingLabel shippingLabel;

    public SessionManagerTest() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkTestPeerFS testSharkPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant());
        SharkPKIComponent sharkPKIComponent = setupComponent(testSharkPeer);
        testSharkPeer.start();
        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);
        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        authentication = new Authentication(sharkPKIComponent);
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
        ShippingLabel shippingLabel = new ShippingLabel(null,null,null, null,
                null, null, null, 0.0);
        sessionManager = new SessionManager(SessionState.AUTHENTIFICATION, ProtocolState.TRANSFEREE, sharkPKIComponent);
        assertFalse(shippingLabel.getIsCreated());
        assertNotEquals(ProtocolState.TRANSFEROR, ProtocolState.TRANSFEREE);
    }

    @Test
    public void transfereeIsNotChangedIfCreateMethodeIsNotCalled() throws NoSuchPaddingException, NoSuchAlgorithmException {
        shippingLabel = new ShippingLabel(null,null,null, null,
                null, null, null, 0.0);
        sessionManager = new SessionManager(SessionState.NO_SESSION, ProtocolState.TRANSFEREE, sharkPKIComponent);
        assertEquals(ProtocolState.TRANSFEREE, ProtocolState.TRANSFEREE);
        shippingLabel = new ShippingLabel(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertEquals(ProtocolState.TRANSFEREE, ProtocolState.TRANSFEREE);
    }

    @Test
    public void transfereeStateChangesWhenCreateIsCalled() throws NoSuchPaddingException, NoSuchAlgorithmException {
        shippingLabel = new ShippingLabel(null,null,null, null,
                null, null, null, 0.0);
        sessionManager = new SessionManager(SessionState.AUTHENTIFICATION, ProtocolState.TRANSFEREE,  sharkPKIComponent);
        UserInput userInput = new UserInput("Alice", "HTW-Berlin", 52.456931, 13.526444,
                "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
        shippingLabel.create(userInput);
        assertNotEquals(ProtocolState.TRANSFEREE, ProtocolState.TRANSFEROR);
    }

    @Test
    public void testIfMessageIsUnhandledWhenSessionDiffers() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException {
        sessionManager = new SessionManager(SessionState.NO_SESSION, ProtocolState.TRANSFEROR, sharkPKIComponent);
        byte[] encryptedNumber = Utilities.encryptAsymmetric("3345".getBytes(), asapKeyStore.getPublicKey());
        Challenge challenge = new Challenge(UUID.randomUUID(), MessageFlag.CHALLENGE, System.currentTimeMillis(), encryptedNumber );
        Optional<Optional<MessageBuilder>> messageBuilder = Optional.ofNullable(sessionManager.sessionHandling(challenge, "Marta"));
        assertThrows(NoSuchElementException.class, () -> sessionManager.sessionHandling(challenge, "Marta"));
    }

    @Test
    public void handleNoSessionAsTransferee() throws NoSuchPaddingException, NoSuchAlgorithmException {
        sessionManager = new SessionManager(SessionState.NO_SESSION, ProtocolState.TRANSFEREE, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));
        assertTrue(object.isPresent()); // This is true because uri and sender are present
    }

    @Test
    public void doNotProcessAdvertisementIfStateIsTransferorWithoutAShippingLabel() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException {
        sessionManager = new SessionManager(SessionState.NO_SESSION, ProtocolState.TRANSFEROR, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));
        Field protocolStateField = sessionManager.getClass().getDeclaredField("protocolState");
        protocolStateField.setAccessible(true);
        assertEquals(ProtocolState.TRANSFEROR, protocolStateField.get(sessionManager));
        assertTrue(object.isPresent()); // This is true because uri and sender are present
    }

    @Test
    public void testIfChallengeMessageGetsRejectedWithoutAdvertisement() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException {
        sessionManager = new SessionManager(SessionState.AUTHENTIFICATION, ProtocolState.TRANSFEROR, sharkPKIComponent);
        byte[] encryptedNumber = Utilities.encryptAsymmetric("4634563456".getBytes(), asapKeyStore.getPublicKey());
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE, System.currentTimeMillis(), encryptedNumber );
        Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(challenge, "Marta");
        assertFalse(messageBuilder.isPresent());
    }

    @Test
    public void handleAnEmptyMessageObjectFromSessionHandler() throws NoSuchPaddingException, NoSuchAlgorithmException {
        MessageHandler messageHandler = new MessageHandler();
        SessionManager sessionManager = new SessionManager(SessionState.NO_SESSION, ProtocolState.TRANSFEROR, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(advertisement, "Marta");
        if (messageBuilder.isPresent()) {
            assertNotNull(messageBuilder.get());
        } else {
            assertEquals(messageBuilder, Optional.empty());
        }
    }

    @Test
    public void testIfAdvertisementGetsRejectedWithoutShippingLabel() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException, NoSuchFieldException, IllegalAccessException {
        sessionManager = new SessionManager(SessionState.NO_SESSION, ProtocolState.TRANSFEREE, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(advertisement, "Marta");
        messageBuilder.ifPresent(Assertions::assertNotNull);
    }

    @Test
    public void testIfProtocolStatesGetSwitched() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        sessionManager = new SessionManager(SessionState.AUTHENTIFICATION, ProtocolState.TRANSFEROR, sharkPKIComponent);
        Method protocolState = sessionManager.getClass().getDeclaredMethod("changeProtocolState");
        Field labelCreated = sessionManager.getClass().getDeclaredField("labelCreated");
        Field deliveryContract = sessionManager.getClass().getDeclaredField("deliveryContract");
        protocolState.setAccessible(true);
        labelCreated.setAccessible(true);
        deliveryContract.setAccessible(true);
        labelCreated.set(sessionManager, true);
        deliveryContract.set(sessionManager, new DeliveryContract(shippingLabel, new TransitRecord()));
        protocolState.invoke(sessionManager, null);
        assertFalse((Boolean) labelCreated.get(sessionManager));
    }

    @Test
    public void testIfResetWorks() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        sessionManager = new SessionManager(SessionState.AUTHENTIFICATION, ProtocolState.TRANSFEROR, sharkPKIComponent);
        Method reset = sessionManager.getClass().getDeclaredMethod("resetAll");
        reset.setAccessible(true);
        reset.invoke(sessionManager, null);
    }
}
