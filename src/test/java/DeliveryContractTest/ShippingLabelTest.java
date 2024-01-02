package DeliveryContractTest;

import DeliveryContract.ShippingLabel;
import HedwigUI.UserInputBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShippingLabelTest {

    private static ShippingLabel shippingLabel = new ShippingLabel();
    private static UserInputBuilder userInputBuilder = new UserInputBuilder("Alice", "HTW-Berlin",
            52.456931, 13.526444, "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
    private static UserInputBuilder nullUserInputBuilder = new UserInputBuilder(null, null, null, null, null, null, null, null, null);

    @Test
    public void testIfCreateReturnsFalseWhenPassedObjectHasNullValue() {
        boolean created = shippingLabel.create(nullUserInputBuilder);
        assertFalse(created);
        assertFalse(shippingLabel.isCreated());
    }

    @Test
    public void testIfCreateReturnsTrueWhenPassedUserDataObjectHasValues() {
        boolean created = shippingLabel.create(userInputBuilder);
        assertTrue(created);
        assertTrue(shippingLabel.isCreated());
    }

    @Test
    public void testIfEmptyShippingLabelReturnsFalseWhenCreatedIsNotCalled() {
        shippingLabel = new ShippingLabel(null, null, null, null, null,
                null, null, 0.0);
        assertFalse(shippingLabel.isCreated());
    }
}
