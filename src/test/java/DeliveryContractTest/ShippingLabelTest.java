package DeliveryContractTest;

import DeliveryContract.*;
import HedwigUI.UserInputObject;
import Location.Location;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShippingLabelTest {

    private static ShippingLabel shippingLabel = new ShippingLabel();
    private static UserInputObject userInputBuilder = new UserInputObject("Alice", "HTW-Berlin",
            52.456931, 13.526444, "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
    private static UserInputObject nullUserInputBuilder = new UserInputObject(null, null, null, null, null, null, null, null, null);

    @Test
    public void testIfCreateReturnsFalseWhenPassedObjectHasNullValue() {
        boolean created = shippingLabel.create(nullUserInputBuilder);
        assertFalse(created);
        assertFalse(shippingLabel.getIsCreated());
    }

    @Test
    public void testIfCreateReturnsTrueWhenPassedUserDataObjectHasValues() {
        assertTrue(shippingLabel.create(userInputBuilder));
        assertTrue(shippingLabel.getIsCreated());
    }

    @Test
    public void testIfEmptyShippingLabelReturnsFalseWhenCreatedIsNotCalled() {
        shippingLabel = new ShippingLabel(null, null, null, null, null,
                null, null, 0.0);
        assertFalse(shippingLabel.getIsCreated());
    }

    @Test
    public void printStringRepresentationOfLabel() {
        ShippingLabel shippingLabel = new ShippingLabel(null, "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof", new Location(52.5105, 13.4346), 1.2);
        System.out.println(shippingLabel.toString());
    }
}
