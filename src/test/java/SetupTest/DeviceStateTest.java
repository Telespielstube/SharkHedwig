package SetupTest;

import DeliveryContract.*;
import Location.Location;
import Session.SessionManager;
import Setup.DeviceState;
import org.junit.Test;

import static Setup.DeviceState.Transferee;
import static Setup.DeviceState.Transferor;
import static org.junit.Assert.*;

public class DeviceStateTest {

    private final SessionManager sessionManager = new SessionManager();
    private ShippingLabel shippingLabel;

    @Test
    public void testIfStateIsTransfereeAfterShippingLabelCreatedButEmpty() {
        shippingLabel = new ShippingLabel();

        assertFalse(ShippingLabel.labelCreated);
        assertEquals(Transferee, DeviceState.Transferee.isActive());
    }

    @Test
    public void testIfStateIsSwitchedToTransferorAfterLabelIsFilledWithContent() {
        shippingLabel = new ShippingLabel(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertTrue(ShippingLabel.labelCreated);
        assertEquals(Transferor, Transferor.isActive());
    }

    @Test
    public void testIfTheSwitchBetweenDeviceStatesInTheSessionManagerIsWorking() {
        DeviceState state = Transferor;
        assertEquals(Transferor, state);
    }


    @Test
    public void checkIfCurrentStateIsTransferor() {
        DeviceState state = DeviceState.Transferor.isActive();

        assertEquals(Transferor, state);
    }

    @Test
    public void assertANotEqualResultIfTransferorAndTransfereeStatesAreSetToTrue() {
        DeviceState state = Transferee.isActive();

        assertEquals(Transferee, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsInActiveAndTransfereeIsActive() {
        DeviceState state = Transferor;
        assertNotEquals(Transferee, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsActiveAndTransfereeIsInactive() {
        DeviceState state = Transferee;
        assertNotEquals(Transferor, state);
    }
}
