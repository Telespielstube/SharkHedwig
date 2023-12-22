package DeliveryContractTest;

import DeliveryContract.ShippingLabel;
import Location.Location;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShippingLabelTest {

    private ShippingLabel shippingLabel;

    @Test
    public void testIfEmptyShippingLabelReturnsFalse() {
        shippingLabel = new ShippingLabel();
        assertFalse(ShippingLabel.labelCreated);
    }

    @Test
    public void testIfShippingLabelReturnsTrue() {
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertTrue(ShippingLabel.labelCreated);
    }
}
