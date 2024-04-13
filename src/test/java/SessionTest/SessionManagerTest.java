package SessionTest;

import Battery.Battery;
import DeliveryContract.*;
import HedwigUI.UserInput;
import Location.Location;
import Message.*;
import Message.Request.Offer;
import Misc.Utilities;
import Session.ReceivedMessageList;
import Session.SessionManager;
import Setup.State.ProtocolState;
import SetupTest.TestConstant;
import Location.*;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SessionManagerTest {

    private SessionManager sessionManager;
    private DeliveryContract deliveryContract;
    private SharkPKIComponent sharkPKIComponent;
    private Battery battery;
    private Locationable geoSpatial;
    private static ASAPKeyStore asapKeyStore;
    private static String francisID;
    private static PublicKey publicKeyFrancis;
    private static ShippingLabel shippingLabel = new ShippingLabel.Builder(null,null,null, null,
            null, null, null, 0.0).build();
    private static ReceivedMessageList receivedMessageList;

    public SessionManagerTest() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkTestPeerFS testSharkPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant());
        SharkPKIComponent sharkPKIComponent = setupComponent(testSharkPeer);
        receivedMessageList = new ReceivedMessageList();
        testSharkPeer.start();
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
    public void testIfTransferorIsUnchangedWhenShippingLabelIsCreatedButEmpty() throws NoSuchPaddingException, NoSuchAlgorithmException {
        ShippingLabel shippingLabel = new ShippingLabel.Builder(null,null,null, null,
                null, null, null, null).build();
        sessionManager = new SessionManager(SessionState_tmp.REQUEST.isActive(), ProtocolState.TRANSFEREE.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        assertFalse(shippingLabel.getIsCreated());
        assertNotEquals(ProtocolState.TRANSFEROR, ProtocolState.TRANSFEREE);
    }

    @Test
    public void transfereeIsNotChangedIfCreateMethodeIsNotCalled() throws NoSuchPaddingException, NoSuchAlgorithmException {
        shippingLabel = new ShippingLabel.Builder(null,null,null, null,
                null, null, null, 0.0).build();
        sessionManager = new SessionManager(SessionState_tmp.NO_SESSION.isActive(), ProtocolState.TRANSFEREE.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        assertEquals(ProtocolState.TRANSFEREE, ProtocolState.TRANSFEREE);
        shippingLabel = new ShippingLabel.Builder(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2).build();
        assertEquals(ProtocolState.TRANSFEREE, ProtocolState.TRANSFEREE);
    }

    @Test
    public void transfereeStateChangesWhenCreateShippingLabelIsCalled() throws NoSuchPaddingException, NoSuchAlgorithmException {
        shippingLabel = new ShippingLabel.Builder(null,null,null, null,
                null, null, null, 0.0).build();
        sessionManager = new SessionManager(SessionState_tmp.REQUEST.isActive(), ProtocolState.TRANSFEREE.isActive(), receivedMessageList, battery, geoSpatial,  sharkPKIComponent);
        UserInput userInput = new UserInput("Alice", "HTW-Berlin", 52.456931, 13.526444,
                "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
        assertNotEquals(ProtocolState.TRANSFEREE, ProtocolState.TRANSFEROR);
    }

    @Test
    public void testIfMessageIsUnhandledWhenSessionDiffers() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException {
        sessionManager = new SessionManager(SessionState_tmp.NO_SESSION.isActive(), ProtocolState.TRANSFEROR.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        Offer offer = new Offer(UUID.randomUUID(), MessageFlag.OFFER, System.currentTimeMillis(), 100, 10.0, new Location("HTW-Berlin", 52.456931, 13.526444) );
        Optional<Optional<MessageBuilder>> messageBuilder = Optional.ofNullable(sessionManager.sessionHandling(offer, "Marta"));
        assertThrows(NullPointerException.class, () -> sessionManager.sessionHandling(offer, "Marta"));
    }

    @Test
    public void handleNoSessionAsTransferee() throws NoSuchPaddingException, NoSuchAlgorithmException {
        sessionManager = new SessionManager(SessionState_tmp.NO_SESSION.isActive(), ProtocolState.TRANSFEREE.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));
        assertTrue(object.isPresent()); // This is true because uri and sender are present
    }

    @Test
    public void doNotProcessAdvertisementIfStateIsTransferorWithoutAShippingLabel() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException {
        sessionManager = new SessionManager(SessionState_tmp.NO_SESSION.isActive(), ProtocolState.TRANSFEROR.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<Object> object = Optional.ofNullable(sessionManager.sessionHandling(advertisement,"Bobby"));
        Field protocolStateField = sessionManager.getClass().getDeclaredField("protocolState");
        protocolStateField.setAccessible(true);
        assertEquals(ProtocolState.TRANSFEROR, protocolStateField.get(sessionManager));
        assertTrue(object.isPresent()); // This is true because uri and sender are present
    }

    @Test
    // Need to rewrite the test with Offer!!!
    public void testIfOfferMessageGetsRejectedWithoutAdvertisement() throws NoSuchPaddingException, NoSuchAlgorithmException, ASAPSecurityException {
        sessionManager = new SessionManager(SessionState_tmp.REQUEST.isActive(), ProtocolState.TRANSFEROR.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        Offer offer = new Offer(UUID.randomUUID(), MessageFlag.OFFER, System.currentTimeMillis(), 100, 10.0, new Location("HTW-Berlin", 52.456931, 13.526444) );
        Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(offer, "Marta");
        assertFalse(messageBuilder.isPresent());
    }

    @Test
    public void handleAnEmptyMessageObjectFromSessionHandler() throws NoSuchPaddingException, NoSuchAlgorithmException {
        MessageHandler messageHandler = new MessageHandler();
        SessionManager sessionManager = new SessionManager(SessionState_tmp.NO_SESSION.isActive(), ProtocolState.TRANSFEROR.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
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
        sessionManager = new SessionManager(SessionState_tmp.NO_SESSION, ProtocolState.TRANSFEREE, receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        Optional<MessageBuilder> messageBuilder = sessionManager.sessionHandling(advertisement, "Marta");
        messageBuilder.ifPresent(Assertions::assertNotNull);
    }

    @Test
    public void testIfProtocolStatesGetChanged() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        sessionManager = new SessionManager(SessionState_tmp.REQUEST.isActive(), ProtocolState.TRANSFEROR.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        Method protocolState = sessionManager.getClass().getDeclaredMethod("changeProtocolState");
        Field labelCreated = sessionManager.getClass().getDeclaredField("shippingLabelCreated");
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
        sessionManager = new SessionManager(SessionState_tmp.REQUEST.isActive(), ProtocolState.TRANSFEROR.isActive(), receivedMessageList, battery, geoSpatial, sharkPKIComponent);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT, Utilities.createTimestamp(), true);
        receivedMessageList.addMessageToList(advertisement);
        Method reset = sessionManager.getClass().getDeclaredMethod("resetAll");
        reset.setAccessible(true);
        reset.invoke(sessionManager, null);
    }
}
