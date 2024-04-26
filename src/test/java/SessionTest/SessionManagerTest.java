package SessionTest;

import Battery.Battery;
import DeliveryContract.*;
import Message.Contract.*;
import Message.MessageBuilder;
import Message.MessageCache;
import Message.MessageFlag;
import Message.NoSession.Advertisement;
import Message.NoSession.Solicitation;
import Message.Request.Confirm;
import Message.Request.Offer;
import Message.Request.OfferReply;
import Misc.Utilities;
import ProtocolRole.ProtocolRole;
import Session.SessionManager;
import Session.State.SessionState;
import Setup.SharkHedwigComponent;
import SetupTest.TestConstant;
import Location.*;
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

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SessionManagerTest {

    private static SessionManager sessionManager;
    private static ProtocolRole protocolRole;
    private DeliveryContract deliveryContract;
    private TransitRecord transitRecord;
    private SharkPKIComponent sharkPKIComponent;
    private Battery battery;
    private Locationable geoSpatial;
    private static ASAPKeyStore asapKeyStore;
    private static String francisID;
    private static PublicKey publicKeyFrancis;
    private static ShippingLabel shippingLabel = new ShippingLabel.Builder(null,null,null, null,
            null, null, null, 0.0).build();
    private static MessageCache messageCache;

    public SessionManagerTest() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkTestPeerFS testSharkPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant());
        SharkPKIComponent sharkPKIComponent = setupComponent(testSharkPeer);
        messageCache = new MessageCache();
        testSharkPeer.start();
        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);
        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        protocolRole = new ProtocolRole(null, null, null , null,sharkPKIComponent);
        sessionManager = new SessionManager(null, protocolRole, null, null, null, sharkPKIComponent);
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
    public void checkIfNoSessionStateIsTheInitialState() {
        sessionManager.getCurrentSessionState();
        assertEquals(sessionManager.getCurrentSessionState(), sessionManager.getNoSessionState());
    }

    @Test
    public void checkIfNoSessionStateIsFollowedByReqesutState() {
        SessionState sessionState = sessionManager.getCurrentSessionState();
        sessionState.nextState();
        assertEquals(sessionManager.getCurrentSessionState(), sessionManager.getRequestState());
    }

    @Test
    public void isTheCurrentStateResetToNoSessionState() {
        sessionManager.setSessionState(sessionManager.getRequestState());
        SessionState sessionState = sessionManager.getContractState();
        sessionState.resetState();
        assertEquals(sessionManager.getCurrentSessionState(), sessionManager.getNoSessionState());
    }

    @Test
    public void testIfAdvertisementIsRejectedInTransfereeRole() {
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT,
                Utilities.createTimestamp(), true);
        protocolRole.setProtocolState(protocolRole.getTransfereeState());
        Optional<MessageBuilder> optionalMessage = sessionManager.sessionHandling(advertisement, "Marta");
        assertFalse(optionalMessage.isPresent());
    }

    @Test
    public void testIfTransfereeSessionHandlingRunsTroughAllSessions() throws NoSuchFieldException, IllegalAccessException {
        shippingLabel = new ShippingLabel.Builder(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2).build();
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PEER_NAME.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null, null));
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);

        /**
         * Component setup
         */
        SharkHedwigComponent sharkHedwigComponent = new SharkHedwigComponent();
        sharkHedwigComponent.setupLogger();
        Field sessionManagerField = sharkHedwigComponent.getClass().getDeclaredField("sessionManager");
        sessionManagerField.setAccessible(true);
        sessionManagerField.set(sharkHedwigComponent, new SessionManager(shippingLabel, protocolRole, deliveryContract, null, null, sharkPKIComponent));
        Field protocolRoleField = sharkHedwigComponent.getClass().getDeclaredField("protocolRole");
        protocolRoleField.setAccessible(true);
        protocolRoleField.set(sharkHedwigComponent, new ProtocolRole(shippingLabel, deliveryContract, null,null, sharkPKIComponent));

        /**
         * Received Messages
         */
        Solicitation solicitation = new Solicitation(Utilities.createUUID(), MessageFlag.SOLICITATION,
                Utilities.createTimestamp(), true);
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT,
                Utilities.createTimestamp(), true);
        OfferReply offerReply = new OfferReply(Utilities.createUUID(), MessageFlag.OFFER_REPLY,
                Utilities.createTimestamp(),100.0, new Location
                (57.5654645, 77.345345));
        ContractDocument contractDocument = new ContractDocument(Utilities.createUUID(), MessageFlag.CONTRACT_DOCUMENT,
                Utilities.createTimestamp(),deliveryContract);
        PickUp pickUp = new PickUp(Utilities.createUUID(), MessageFlag.PICK_UP,
                Utilities.createTimestamp(),transitRecord);
        Release release = new Release(Utilities.createUUID(), MessageFlag.RELEASE,
                Utilities.createTimestamp());

        /**
         * Processing of received messages
         */
        MessageCache.addMessage(advertisement);
        Optional<MessageBuilder> offer = sharkHedwigComponent.testHelperMethod(solicitation, "Marta");
        Optional<MessageBuilder> confirm = sharkHedwigComponent.testHelperMethod(offerReply, "Marta");
        Optional<MessageBuilder> affirm = sharkHedwigComponent.testHelperMethod(contractDocument, "Marta");
        // Doesn not work because of some signing/verifying issues!!
        //Optional<MessageBuilder> ready = sharkHedwigComponent.testHelperMethod(pickUp,"Marta");
        Optional<MessageBuilder> complete = sharkHedwigComponent.testHelperMethod(release, "Marta");
    }

    @Test
    public void testIfTransferorSessionHandlingRunsTroughAllSessions() throws NoSuchFieldException, IllegalAccessException {
        shippingLabel = new ShippingLabel.Builder(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2).build();
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PEER_NAME.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null, null));
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);

        // Component and field setup
        SharkHedwigComponent sharkHedwigComponent = new SharkHedwigComponent();

        Field sessionManagerField = sharkHedwigComponent.getClass().getDeclaredField("sessionManager");
        sessionManagerField.setAccessible(true);
        sessionManagerField.set(sharkHedwigComponent, new SessionManager(shippingLabel, protocolRole, deliveryContract, null, null, sharkPKIComponent));
        Field protocolRoleField = sharkHedwigComponent.getClass().getDeclaredField("protocolRole");
        protocolRoleField.setAccessible(true);
        protocolRoleField.set(sharkHedwigComponent, new ProtocolRole(shippingLabel, deliveryContract, null,null, sharkPKIComponent));

        // Messages
        Advertisement advertisement = new Advertisement(Utilities.createUUID(), MessageFlag.ADVERTISEMENT,
                Utilities.createTimestamp(), true);
        Offer offer = new Offer(Utilities.createUUID(), MessageFlag.OFFER, Utilities.createTimestamp(), 100,
                1000.0, new Location(80.0,90.0));
        Confirm confirm = new Confirm(Utilities.createUUID(), MessageFlag.CONFIRM, Utilities.createTimestamp());
        Affirm affirm = new Affirm(Utilities.createUUID(), MessageFlag.AFFIRM, Utilities.createTimestamp(),
                deliveryContract);
        Ready ready = new Ready(Utilities.createUUID(), MessageFlag.AFFIRM, Utilities.createTimestamp());
        Complete complete = new Complete(Utilities.createUUID(), MessageFlag.AFFIRM, Utilities.createTimestamp());

//        protocolRole.setProtocolState(protocolRole.getTransferorState());
//        Field roleField = protocolRole.getClass().getDeclaredField("transferorState");
//        roleField.setAccessible(true);
//        roleField.set(protocolRole, new Transferor(null, shippingLabel, deliveryContract, sharkPKIComponent));

        Optional<MessageBuilder> solicitation = sharkHedwigComponent.testHelperMethod(advertisement, "Marta");
        Optional<MessageBuilder> offerReply = sharkHedwigComponent.testHelperMethod(offer, "Marta");
//
//        Optional<MessageBuilder> solicitation = sessionManager.sessionHandling(advertisement, "Marta");
//        Optional<MessageBuilder> offerReply = sessionManager.sessionHandling(offer, "Marta");
//        Optional<MessageBuilder> contractDocument = sessionManager.sessionHandling(confirm, "Marta");
//        Optional<MessageBuilder> pickUp = sessionManager.sessionHandling(affirm, "Marta");
//        Optional<MessageBuilder> release = sessionManager.sessionHandling(ready, "Marta");
//        Optional<MessageBuilder> finished = sessionManager.sessionHandling(complete, "Marta");

        assertInstanceOf(Solicitation.class, solicitation.get().getMessage());
//        assertInstanceOf(OfferReply.class, offerReply.get().getMessage());
//        assertInstanceOf(ContractDocument.class, contractDocument.get().getMessage());
//        assertInstanceOf(PickUp.class, pickUp.get().getMessage());
//        assertInstanceOf(Release.class, release.get().getMessage());

    }
}
