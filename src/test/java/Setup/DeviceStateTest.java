package Setup;

import DeliveryContract.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeviceStateTest {

    @Test
    public void testIfStateIsSwitchedToTransferorAfterDeliveryContractIsCreated() {
        new DeliveryContract(new ShippingLabel(), new TransitRecord());
        assertTrue(DeliveryContract.contractCreated);
    }

    @Test
    public void testIfStateIsTransfereeAsLongAsNoDeliveryContractIsCreated() {
         DeviceState state = DeviceState.Transferee.isActive();
         new DeliveryContract();
         assertFalse(DeliveryContract.contractCreated);
    }
}
