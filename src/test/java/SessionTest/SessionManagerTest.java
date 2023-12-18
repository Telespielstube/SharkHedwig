package SessionTest;

import DeliveryContract.*;
import Session.SessionManager;
import Session.SessionState;
import Setup.DeviceState;
import org.junit.Test;

import static org.junit.Assert.*;

public class SessionManagerTest {

//    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());
//    private final Response response = new Response(UUID.randomUUID(), System.currentTimeMillis());
//    private final Contract contract = new Contract();
    private final SessionManager sessionManager = new SessionManager(null, SessionState.NoSession, DeviceState.Transferee, null, null);

    @Test
    public void testIfDeviceTransferorStateReturnsFalseWhenDeliveryContractIsCreatedButEmpty() {
        DeliveryContract deliveryContract = new DeliveryContract();
        assertFalse(sessionManager.checkTransferorState());
    }

    @Test
    public void testIfTransferorIsSetAndTrueWhenDeliveryContractIsCreated() {
        assertFalse(sessionManager.checkTransferorState());
        DeliveryContract deliveryContract = new DeliveryContract(new ShippingLabel(), new TransitRecord());
        assertTrue(sessionManager.checkTransferorState());
    }

}
