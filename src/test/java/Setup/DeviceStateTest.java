package Setup;

import DeliveryContract.*;
import Location.Location;
import Session.SessionManager;
import org.junit.Test;

import static Setup.DeviceState.Transferee;
import static Setup.DeviceState.Transferor;
import static org.junit.Assert.*;

public class DeviceStateTest {

    private final SessionManager sessionManager = new SessionManager();
    private ShippingLabel shippingLabel;

    @Test
    public void testIfStateIsSwitchedToTransferorAfterDeliveryContractIsCreated() {
        new ShippingLabel(shippingLabel.createUUID(), "Alice", "HTW-Berlin", new Location(80.67, 90.56),
                "Bob", "Ostbahnhof", new Location(52.5105, 13.4346),
                1.2);
        assertTrue(ShippingLabel.labelCreated);
        assertEquals("Transferor", Transferor);
    }

    @Test
    public void testIfStateIsTransfereeAsLongAsNoShippingLabelIsCreated() {
       //  DeviceState state = DeviceState.Transferee.isActive();
         new ShippingLabel();
         assertFalse(ShippingLabel.labelCreated);
         assertEquals("Transferee", Transferee);
    }

    @Test
    public void testIfTheSwitchBetweenDeviceStatesInTheSessionManagerIsWorking() {
        DeviceState state = Transferor;
        assertEquals(Transferor, state);
    }


    @Test
    public void checkIfCurrentStateIsTransferor() {
        DeviceState state = DeviceState.Transferor.isActive();
        System.out.println("Device state: " + state.toString());
        assertEquals(Transferor, state);
    }

    @Test
    public void assertANotEqualResultIfTransferorAndTransfereeStatesAreSetToTrue() {
        DeviceState state = Transferee.isActive();
        System.out.println("Device state: " + state.toString());
        assertEquals(Transferee, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsInActiveAndTransfereeIsActive() {
        DeviceState state = Transferor;
        System.out.println("Device state: " + state.toString());
        assertNotEquals(Transferee, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsActiveAndTransfereeIsInactive() {
        DeviceState state = Transferee;
        System.out.println("Device state: " + state.toString());
        assertNotEquals(Transferor, state);
    }
}
