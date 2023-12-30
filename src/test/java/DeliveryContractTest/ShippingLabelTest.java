package DeliveryContractTest;

import DeliveryContract.ShippingLabel;
import Location.Location;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShippingLabelTest {

    private static ShippingLabel shippingLabel;

    @BeforeAll
    public static void setup() {
//        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
//                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
//                new Location(52.5105, 13.4346), 1.2);
//        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Bob", "HTW-Berlin",
//                new Location(52.456931, 13.526444), "Sandy", "TU-Berlin",
//                new Location(52.7705, 13.9946), 0.9);
    }

    @Test
    public void testIfEmptyShippingLabelReturnsFalse() {
        shippingLabel = new ShippingLabel(null, null, null, null, null,
                null, null, 0.0);
        assertFalse(shippingLabel.isCreated());
    }

    @Test
    public void testIfShippingLabelFilledWithNullIsFalse() {
        shippingLabel = new ShippingLabel(null,null, null, null, null,
                null, null, 0.0);
        assertFalse(shippingLabel.isCreated());
    }

    @Test
    public void testIfShippingLabelReturnsTrue() {
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertTrue(shippingLabel.isCreated());
    }
}
