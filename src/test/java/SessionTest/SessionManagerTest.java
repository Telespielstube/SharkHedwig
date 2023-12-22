package SessionTest;

import Location.Location;
import DeliveryContract.ShippingLabel;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Message.MessageBuilder;
import Message.MessageFlag;
import Message.MessageHandler;
import Session.SessionManager;
import Session.SessionState;
import Session.Sessions.Identification;
import Setup.DeviceState;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.asap.crypto.ASAPKeyStore;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.UUID;


import static net.sharksystem.asap.apps.testsupport.TestConstants.ALICE_ID;
import static org.junit.Assert.*;

public class SessionManagerTest {

    private SessionManager sessionManager = new SessionManager(null, SessionState.NoSession, DeviceState.Transferee, null, null);
    private Response response;
    private MessageHandler messageHandler = new MessageHandler();

    public SessionManagerTest() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }


    @Test
    public void testIfDeviceTransferorStateReturnsFalseWhenShippingLabelIsCreatedButEmpty() {
        ShippingLabel shippingLabel = new ShippingLabel();
        assertFalse(sessionManager.checkTransferorState());
    }

    @Test
    public void testIfTransferorIsSetAndTrueWhenDeliveryContractIsCreated() {
        assertFalse(sessionManager.checkTransferorState());
        new ShippingLabel(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertTrue(sessionManager.checkTransferorState());
    }
}
