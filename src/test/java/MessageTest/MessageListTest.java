package MessageTest;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import DeliveryContract.TransitRecord;
import Message.Contract.Affirm;
import Message.Contract.ContractDocument;
import Message.Contract.Ready;
import Message.MessageFlag;
import Misc.Utilities;
import Message.MessageList;
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

public class MessageListTest {
    private static MessageList messageList;
    private static String francisID;
    private static SharkPKIComponent sharkPKIComponent;
    private static ASAPKeyStore asapKeyStore;

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkTestPeerFS testSharkPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant());
        sharkPKIComponent = setupComponent(testSharkPeer);
        messageList = new MessageList();
        testSharkPeer.start();

        String idStart = HelperPKITests.fillWithExampleData(sharkPKIComponent);

        asapKeyStore = sharkPKIComponent.getASAPKeyStore();
        francisID = HelperPKITests.getPeerID(idStart, HelperPKITests.FRANCIS_NAME);
        PublicKey publicKeyFrancis = asapKeyStore.getPublicKey(francisID);
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
        Ready ready = new Ready(Utilities.createUUID(), MessageFlag.AFFIRM, 1705740810); // fixed timestamp Saturday, 9:58, 20th 2014
        messageList.addMessageToList(ready);
        assertTrue(messageList.compareTimestamp(1705741000, 300));
    }

    @Test
    public void compareIfTimestampIsInRange() {
        Ready ready = new Ready(Utilities.createUUID(), MessageFlag.READY_TO_PICK_UP, Utilities.createTimestamp()); // fixed timestamp Saturday, 9:58, 20th 2014
        messageList.addMessageToList(ready);
        assertTrue(messageList.compareTimestamp(Utilities.createTimestamp(), 3000));
    }
}
