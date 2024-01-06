package SetupTest;

import DeliveryContract.*;
import Location.Location;
import Session.SessionManager;
import Setup.ProtocolState;
import org.junit.jupiter.api.Test;

import static Setup.ProtocolState.Transferee;
import static Setup.ProtocolState.Transferor;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocolStateTest {

    private final SessionManager sessionManager = new SessionManager();
    private ShippingLabel shippingLabel;

    @Test
    public void testIfStateIsTransfereeAfterShippingLabelCreatedButEmpty() {
        shippingLabel = new ShippingLabel(null, null, null, null, null,
                null, null, 0.0);

        assertFalse(shippingLabel.getIsCreated());
        assertEquals(Transferee, ProtocolState.Transferee.isActive());
    }

    @Test
    public void testIfStateIsSwitchedToTransferorAfterLabelIsFilledWithContent() {
        shippingLabel = new ShippingLabel(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertFalse(shippingLabel.getIsCreated());
        assertEquals(Transferor, Transferor.isActive());
    }

    @Test
    public void testIfTheSwitchBetweenDeviceStatesInTheSessionManagerIsWorking() {
        ProtocolState state = Transferor;
        assertEquals(Transferor, state);
    }


    @Test
    public void checkIfCurrentStateIsTransferor() {
        ProtocolState state = ProtocolState.Transferor.isActive();

        assertEquals(Transferor, state);
    }

    @Test
    public void assertANotEqualResultIfTransferorAndTransfereeStatesAreSetToTrue() {
        ProtocolState state = Transferee.isActive();

        assertEquals(Transferee, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsInActiveAndTransfereeIsActive() {
        ProtocolState state = Transferor;
        assertNotEquals(Transferee, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsActiveAndTransfereeIsInactive() {
        ProtocolState state = Transferee;
        assertNotEquals(Transferor, state);
    }
}
