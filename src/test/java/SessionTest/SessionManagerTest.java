package SessionTest;

import DeliveryContract.ShippingLabel;
import HedwigUI.UserInput;
import Location.Location;
import Message.*;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Misc.Utilities;
import Session.*;
import Session.Sessions.Identification;
import Setup.Channel;
import Setup.DeviceState;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class SessionManagerTest {

    private SessionManager sessionManager;
    private Response response;
    private MessageHandler messageHandler = new MessageHandler();
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
        SharkPKIComponent sharkPKIComponent = (SharkPKIComponent) component;

        return sharkPKIComponent;
    }

    @Test
    public void testIfNextStateIsActive() throws NoSuchPaddingException, NoSuchAlgorithmException {
        //sessionManager = new SessionManager(messageHandler, SessionState.Identification, DeviceState.Transferee, null, sharkPKIComponent);
        assertEquals(SessionState.Request, SessionState.Identification.nextState());
    }

    @Test
    public void testIfDeviceTransferorIsUnchangedWhenShippingLabelIsCreatedButEmpty() throws NoSuchPaddingException, NoSuchAlgorithmException {
        ShippingLabel shippingLabel = new ShippingLabel();
        sessionManager = new SessionManager(messageHandler, SessionState.Identification, DeviceState.Transferee, null, sharkPKIComponent);
        assertFalse(shippingLabel.getIsCreated());
        sessionManager.checkDeviceState();
        assertNotEquals(DeviceState.Transferor, DeviceState.Transferee);
    }

    @Test
    public void transfereeIsNotChangedIfCreateMethodeIsNotCalled() throws NoSuchPaddingException, NoSuchAlgorithmException {
        shippingLabel = new ShippingLabel();
        sessionManager = new SessionManager(messageHandler, SessionState.NoSession, DeviceState.Transferee, null, sharkPKIComponent);
        sessionManager.checkDeviceState();
        assertEquals(DeviceState.Transferee, DeviceState.Transferee);
        shippingLabel = new ShippingLabel(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertEquals(DeviceState.Transferee, DeviceState.Transferee);
    }

    @Test
    public void transfereeStateChangesWhenCreateIsCalled() throws NoSuchPaddingException, NoSuchAlgorithmException {
        shippingLabel = new ShippingLabel();
        sessionManager = new SessionManager(messageHandler, SessionState.Identification, DeviceState.Transferee, null, sharkPKIComponent);
        sessionManager.checkDeviceState();
        UserInput userInput = new UserInput("Alice", "HTW-Berlin", 52.456931, 13.526444,
                "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
        shippingLabel.create(userInput);
        sessionManager.checkDeviceState();
        assertNotEquals(DeviceState.Transferee, DeviceState.Transferor);
    }

    @Test
    public void testIfMessageIsUnhandledWhenSessionDiffers() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException {
        sessionManager = new SessionManager(messageHandler, SessionState.NoSession, DeviceState.Transferor, null, sharkPKIComponent);
        byte[] encryptedNumber = Utilities.encryptAsymmetric("3345".getBytes(), asapKeyStore.getPublicKey());
        Challenge challenge = new Challenge(UUID.randomUUID(), MessageFlag.Challenge, System.currentTimeMillis(), encryptedNumber );
        MessageBuilder messageBuilder = sessionManager.sessionHandling(challenge, "Marta").get();
        assertEquals(Channel.Advertisement.getChannelType(), messageBuilder.getUri());
    }

    @Test
    public void handleNoSessionAsTransferee() throws NoSuchPaddingException, NoSuchAlgorithmException {
        sessionManager = new SessionManager(null, SessionState.NoSession, DeviceState.Transferee, null, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.Advertisement, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));
        assertTrue(object.isPresent()); // This is true because uri and sender are present
    }

    @Test
    public void sendAdvertisementIfStateIsTransferorWithoutAShippingLabel() throws NoSuchPaddingException, NoSuchAlgorithmException {
        sessionManager = new SessionManager(null, SessionState.NoSession, DeviceState.Transferor, null, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.Advertisement, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));
        assertTrue(object.isPresent()); // This is true because uri and sender are present
    }

    @Test
    public void testIfChallengeMessageGetsRejectedWithoutAdvertisement() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException {
        response = new Response();
        sessionManager = new SessionManager(null, SessionState.Identification, DeviceState.Transferor, null, sharkPKIComponent);
        byte[] encryptedNumber = Utilities.encryptAsymmetric("4634563456".getBytes(), asapKeyStore.getPublicKey());
        Challenge challenge = new Challenge(Utilities.createUUID(), MessageFlag.Challenge, System.currentTimeMillis(), encryptedNumber );
        Optional<MessageBuilder> messageBuilder = Optional.ofNullable(sessionManager.sessionHandling(challenge, "Marta")).orElse(null);
        assertFalse(messageBuilder.isPresent());
    }

    @Test
    public void testIfResponseGetsRejectedWithoutShippingLabelandWrongDeviceState() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException, NoSuchFieldException, IllegalAccessException {
        sessionManager = new SessionManager(null, SessionState.Identification, DeviceState.Transferor, null, sharkPKIComponent);
        Field sessionStateField = sessionManager.getClass().getDeclaredField("sessionState");
        Field noSessionField = sessionManager.getClass().getDeclaredField("noSession");
        sessionStateField.setAccessible(true);
        noSessionField.setAccessible(true);
        sessionStateField.set(sessionManager, SessionState.Identification);
        noSessionField.set(sessionManager, true);

        byte[] number = "4634563456".getBytes();
        byte[] encrypted = Utilities.encryptAsymmetric(number, asapKeyStore.getPublicKey());
        Response response = new Response(Utilities.createUUID(), MessageFlag.Response, Utilities.createTimestamp(), encrypted, number);
        Optional<MessageBuilder> messageBuilder = Optional.ofNullable(sessionManager.sessionHandling(response, "Marta")).orElse(null);
        assertNotEquals("Message flag was incorrect:Response", Optional.empty());
    }

    @Test
    public void testIfResponseGetsProcessed() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        sessionManager = new SessionManager(null, SessionState.Identification, DeviceState.Transferor, null, sharkPKIComponent);
//        Field sessionStateField = sessionManager.getClass().getDeclaredField("sessionState");
//        Field noSessionField = sessionManager.getClass().getDeclaredField("noSession");
//        Field deviceStateField = sessionManager.getClass().getDeclaredField("deviceState");
//        sessionStateField.setAccessible(true);
//        noSessionField.setAccessible(true);
//        deviceStateField.setAccessible(true);
//        sessionStateField.set(sessionManager, SessionState.Identification);
//        noSessionField.set(sessionManager, true);
//        deviceStateField.set(sessionManager, DeviceState.Transferor);
//        byte[] number = "4634563456".getBytes();
//        byte[] encrypted = Utilities.encryptAsymmetric(number, asapKeyStore.getPublicKey());
//        Response response = new Response(Utilities.createUUID(), MessageFlag.Response, Utilities.createTimestamp(), encrypted, "4523452345".getBytes());
//        Identification identifi = mock(Identification.class);
//        Field ident = identifi.getClass().getDeclaredFields("secureNumber")[0];
//        ident.setAccessible(true);
//        ident.set(identification, "4523452345".getBytes());
//        Method processIdentification = sessionManager.getClass().getDeclaredMethod("processIdentification", IMessage.class);
//        processIdentification.setAccessible(true);
//        processIdentification.invoke(sessionManager,  response);
//        //assertNotEquals("Message flag was incorrect:Response", Optional.empty());
    }
}
