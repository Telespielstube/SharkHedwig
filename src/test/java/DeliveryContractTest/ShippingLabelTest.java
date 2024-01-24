package DeliveryContractTest;

import DeliveryContract.*;
import HedwigUI.UserInput;
import Location.Location;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShippingLabelTest {

    private static ShippingLabel shippingLabel = new ShippingLabel(null,null,null, null,
            null, null, null, 0.0);
    private static final UserInput userInputBuilder = new UserInput("Alice", "HTW-Berlin",
            52.456931, 13.526444, "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
    private static final UserInput nullUserInputBuilder = new UserInput(null, null, null, null, null, null, null, null, null);

    @Test
    public void testIfCreateReturnsFalseWhenPassedObjectHasNullValue() {
        boolean created = shippingLabel.create(nullUserInputBuilder);
        assertFalse(created);

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

    @Test
    public void testIfUserInputIsVerified() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method verify = shippingLabel.getClass().getDeclaredMethod("verifyUserData", UserInput.class);
        verify.setAccessible(true);
        assertTrue((Boolean) verify.invoke(shippingLabel, userInputBuilder));
    }
}
