package SessionTest;

import DeliveryContract.*;
import Location.Location;
import Message.Contract.Confirm;
import Message.Contract.ContractDocument;
import Message.MessageFlag;
import Message.MessageHandler;
import Misc.Utilities;
import Session.Sessions.Contract;
import Session.Sessions.Identification;
import SetupTest.TestConstant;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ContractTest {

    private static Contract contract;
    private static ContractDocument contractDocument;
    private static TransitRecord transitRecord;
    private static DeliveryContract deliveryContract;
    private static ShippingLabel shippingLabel;
    private static String francisID;
    private static SharkPKIComponent sharkPKIComponent;
    private static ASAPKeyStore asapKeyStore;

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkTestPeerFS testSharkPeer = new SharkTestPeerFS(TestConstant.PeerName.getTestConstant(), TestConstant.PeerFolder.getTestConstant());
        sharkPKIComponent = setupComponent(testSharkPeer);

        testSharkPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        PublicKey publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        Identification identification = new Identification(sharkPKIComponent);
        MessageHandler messageHandler = new MessageHandler();
        contract = new Contract(sharkPKIComponent);
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PeerName.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PeerName.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PeerName.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null, null));
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        ContractDocument contractDocument;

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
    public void testIfTransitRecordSignaturesAreCorrect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        contractDocument = new ContractDocument(Utilities.createUUID(), MessageFlag.ContractDocument, Utilities.createTimestamp(), deliveryContract);
        Method handleContract = contract.getClass().getDeclaredMethod("handleContract", ContractDocument.class);
        handleContract.setAccessible(true);
        Optional<Confirm> message = (Optional<Confirm>) handleContract.invoke(contract, contractDocument);
        assertNotNull(message);
        message.ifPresent(confirm -> assertEquals(confirm.getClass(), Confirm.class));
        message.ifPresent((o -> assertNotNull(message.get().getDeliveryContract().getTransitRecord().getLastElement().getSignatureTransferee())));
    }

    @Test
    public void testIfLastElementIsReturned() {
        TransitEntry entry = transitRecord.getLastElement();
        assertNotNull(entry);
        assertEquals(4, entry.getSerialNumber());
        System.out.println(entry.getTransferee());
        assertEquals("Bob", entry.getTransferee());
    }

    @Test
    public void testIfTransfereeIsSet() {
        TransitEntry entry = transitRecord.getLastElement();
        entry.setTransferee("Bruce");
        assertEquals("Bruce", entry.getTransferee());
    }


}