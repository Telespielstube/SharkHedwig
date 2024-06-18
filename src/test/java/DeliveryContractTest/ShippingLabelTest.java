package DeliveryContractTest;

import DeliveryContract.ShippingLabel;
import Location.Location;
import Misc.Utilities;
import org.junit.jupiter.api.Test;

public class ShippingLabelTest {


    private static final ShippingLabel shippingLabel = new ShippingLabel(null,null,null, null,
            null, null, null, null);

    @Test
    public void printStringRepresentationOfLabel() {
        ShippingLabel shippingLabel = new ShippingLabel(Utilities.createUUID(), "Alice", "HTW-Berlin",
                new Location(52.456931, 13.526444), "Bob", "Ostbahnhof", new Location(52.5105, 13.4346), 1.2);
        System.out.println(shippingLabel.toString());
    }
}
