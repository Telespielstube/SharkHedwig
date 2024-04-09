package SetupTest;

import DeliveryContract.*;
import Location.Location;
import Setup.ProtocolState;
import org.junit.jupiter.api.Test;

import static Setup.ProtocolState.TRANSFEREE;
import static Setup.ProtocolState.TRANSFEROR;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocolStateTest {

    private ShippingLabel shippingLabel;

    @Test
    public void testIfStateIsTransfereeAfterShippingLabelCreatedButEmpty() {
        shippingLabel = new ShippingLabel.Builder(null, null, null, null, null,
                null, null, 0.0).build();

        assertFalse(shippingLabel.getIsCreated());
        assertEquals(TRANSFEREE, ProtocolState.TRANSFEREE.isActive());
    }

    @Test
    public void testIfStateIsSwitchedToTransferorAfterLabelIsFilledWithContent() {
        shippingLabel = new ShippingLabel.Builder(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2).build();
        assertFalse(shippingLabel.getIsCreated());
        assertEquals(TRANSFEROR, TRANSFEROR.isActive());
    }

    @Test
    public void testIfTheSwitchBetweenDeviceStatesInTheSessionManagerIsWorking() {
        ProtocolState state = TRANSFEROR;
        assertEquals(TRANSFEROR, state);
    }


    @Test
    public void checkIfCurrentStateIsTransferor() {
        ProtocolState state = ProtocolState.TRANSFEROR.isActive();

        assertEquals(TRANSFEROR, state);
    }

    @Test
    public void assertANotEqualResultIfTransferorAndTransfereeStatesAreSetToTrue() {
        ProtocolState state = TRANSFEREE.isActive();

        assertEquals(TRANSFEREE, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsInActiveAndTransfereeIsActive() {
        ProtocolState state = TRANSFEROR;
        assertNotEquals(TRANSFEREE, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsActiveAndTransfereeIsInactive() {
        ProtocolState state = TRANSFEREE;
        assertNotEquals(TRANSFEROR, state);
    }
}
