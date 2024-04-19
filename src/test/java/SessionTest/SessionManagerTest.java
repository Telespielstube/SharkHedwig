package SessionTest;

import Battery.Battery;
import DeliveryContract.*;
import HedwigUI.UserInput;
import Location.Location;
import Message.*;
import Message.NoSession.Advertisement;
import Message.Request.Offer;
import Misc.Utilities;
import Message.MessageList;
import Session.SessionManager;
import ProtocolRole.State.ProtocolState;
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
    private static MessageList messageList;

    public SessionManagerTest() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    @BeforeAll
    public static void setup() throws SharkException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        SharkTestPeerFS testSharkPeer = new SharkTestPeerFS(TestConstant.PEER_NAME.getTestConstant(), TestConstant.PEER_FOLDER.getTestConstant());
        SharkPKIComponent sharkPKIComponent = setupComponent(testSharkPeer);
        messageList = new MessageList();
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
}
