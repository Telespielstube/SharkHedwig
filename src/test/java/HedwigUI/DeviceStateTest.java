package HedwigUI;

import org.junit.Before;
import org.junit.Test;


import static HedwigUI.DeviceState.Transferor;
import static HedwigUI.DeviceState.Transferee;
import static org.junit.Assert.*;

public class DeviceStateTest {

    @Test
    public void checkIfCurentStateIsTransferor() {
        DeviceState state = Transferor.isActive();
        System.out.println("Device state: " + state.toString());
        assertEquals(Transferor.isActive(), state);
    }

    @Test
    public void assertANotEqualResultIfTransferorAndTransfereeStatesAreSetToTrue() {
        DeviceState state = Transferee.isActive();
        System.out.println("Device state: " + state.toString());
        assertNotEquals(Transferor.isActive(), state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsInActiveAndTransfereeIsActive() {
        DeviceState state = Transferor.isInactive();
        System.out.println("Device state: " + state.toString());
        assertEquals(Transferee.isActive(), state);
    }

    @Test
    public void returnEqualsIfTransferorStateIsActiveAndTransfereeIsInactive() {
        DeviceState state = Transferee.isInactive();
        System.out.println("Device state: " + state.toString());
        assertEquals(Transferor.isActive(), state);
    }
}