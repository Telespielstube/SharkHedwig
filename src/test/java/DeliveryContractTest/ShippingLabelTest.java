package DeliveryContractTest;

import DeliveryContract.ShippingLabel;
import Location.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShippingLabelTest {

    private ShippingLabel shippingLabel;

    @Before
    public void setup() {
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Bob", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Sandy", "TU-Berlin",
                new Location(52.7705, 13.9946), 0.9);
    }

    @Test
    public void testIfEmptyShippingLabelReturnsFalse() {
        shippingLabel = new ShippingLabel();
        assertFalse(shippingLabel.getIsCreated());
    }

    @Test
    public void testIfShippingLabelFilledWithNullIsFalse() {
        shippingLabel = new ShippingLabel(null,null, null, null, null,
                null, null, 0.0);
        assertFalse(shippingLabel.getIsCreated());
    }
    @Test
    public void testIfShippingLabelReturnsTrue() {
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertTrue(shippingLabel.getIsCreated());
    }
}
