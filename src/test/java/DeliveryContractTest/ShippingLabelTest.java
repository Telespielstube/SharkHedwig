package DeliveryContractTest;

import DeliveryContract.*;
import HedwigUI.UserInput;
import HedwigUI.UserManager;
import Location.Location;
import Misc.Utilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ShippingLabelTest {

    private static final UserManager manager = new UserManager();
    private static final ShippingLabel shippingLabel = new ShippingLabel.Builder(null,null,null, null,
            null, null, null, null).build();
    private static final UserInput userInputBuilder = new UserInput("Alice", "HTW-Berlin",
            52.456931, 13.526444, "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
    private static final UserInput nullUserInputBuilder = new UserInput(null, null, null, null, null, null, null, null, null);

    @Test
    public void printStringRepresentationOfLabel() {
        ShippingLabel shippingLabel = new ShippingLabel.Builder(Utilities.createUUID(), "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof", new Location(52.5105, 13.4346), 1.2).build();
        System.out.println(shippingLabel.toString());
    }
}
