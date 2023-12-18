package Setup;

import DeliveryContract.*;
import Session.SessionManager;
import org.junit.Test;

import static Setup.DeviceState.Transferee;
import static Setup.DeviceState.Transferor;
import static org.junit.Assert.*;

public class DeviceStateTest {

    private final SessionManager sessionManager = new SessionManager();
    @Test
    public void testIfStateIsSwitchedToTransferorAfterDeliveryContractIsCreated() {
        new DeliveryContract(new ShippingLabel(), new TransitRecord());
        assertTrue(DeliveryContract.contractCreated);
    }

    @Test
    public void testIfStateIsTransfereeAsLongAsNoDeliveryContractIsCreated() {
       //  DeviceState state = DeviceState.Transferee.isActive();
         new DeliveryContract();
         assertFalse(DeliveryContract.contractCreated);
    }

    @Test
    public void testIfTheSwitchBetweenDeviceStatesInTheSessionManagerIsWorking() {
        DeviceState state = Transferor;
        assertEquals(Transferor, state);
    }


    @Test
    public void checkIfCurentStateIsTransferor() {
        DeviceState state = DeviceState.Transferor.isActive();
        System.out.println("Device state: " + state.toString());
        assertEquals(Transferor, state);
    }

    @Test
    public void assertANotEqualResultIfTransferorAndTransfereeStatesAreSetToTrue() {
        DeviceState state = Transferee.isActive();
        System.out.println("Device state: " + state.toString());
        assertEquals(Transferee, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsInActiveAndTransfereeIsActive() {
        DeviceState state = Transferor;
        System.out.println("Device state: " + state.toString());
        assertNotEquals(Transferee, state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsActiveAndTransfereeIsInactive() {
        DeviceState state = Transferee;
        System.out.println("Device state: " + state.toString());
        assertNotEquals(Transferor, state);
    }
}
