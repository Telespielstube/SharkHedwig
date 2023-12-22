package SessionTest;

import Location.Location;
import DeliveryContract.ShippingLabel;
import Session.SessionManager;
import Session.SessionState;
import Setup.DeviceState;
import org.junit.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class SessionManagerTest {

    private final SessionManager sessionManager = new SessionManager(null, SessionState.NoSession, DeviceState.Transferee, null, null);

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
