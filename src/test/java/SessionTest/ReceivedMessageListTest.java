package SessionTest;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import DeliveryContract.TransitRecord;
import Message.Contract.Ack;
import Message.Contract.ContractDocument;
import Message.MessageFlag;
import Misc.Utilities;
import Session.Contract;
import Session.ReceivedMessageList;
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
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReceivedMessageListTest {

    private static Contract contract;
    private static ContractDocument contractDocument;
    private static TransitRecord transitRecord;
    private static DeliveryContract deliveryContract;
    private static ShippingLabel shippingLabel;
    private static ReceivedMessageList receivedMessageList;
    private static String francisID;
    private static SharkPKIComponent sharkPKIComponent;
    private static ASAPKeyStore asapKeyStore;

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkTestPeerFS testSharkPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant());
        sharkPKIComponent = setupComponent(testSharkPeer);
        receivedMessageList = new ReceivedMessageList();
        testSharkPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        PublicKey publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
        contract = new Contract(sharkPKIComponent, receivedMessageList);
    }

    private static SharkPKIComponent setupComponent(SharkPeer sharkPeer) throws SharkException {
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
    public void compareIfTimestampISOutOFRange() {
        Ack ack = new Ack(Utilities.createUUID(), MessageFlag.ACK, 1705740810, true); // fixed timestamp Saturday, 9:58, 20th 2014
        receivedMessageList.addMessageToList(ack);
        assertTrue(receivedMessageList.compareTimestamp(1705741000, 300));
    }

    @Test
    public void compareIfTimestampIsInRange() {
        Ack ack = new Ack(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true); // fixed timestamp Saturday, 9:58, 20th 2014
        receivedMessageList.addMessageToList(ack);
        assertTrue(receivedMessageList.compareTimestamp(Utilities.createTimestamp(), 3000));
    }

    @Test
    public void testIfSessionIsSetComplete() {
        Ack ack = new Ack(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true); // fixed timestamp Saturday, 9:58, 20th 2014
        receivedMessageList.addMessageToList(ack);
        contract.setSessionComplete(true);
    }
}
