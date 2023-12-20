package DeliveryContract;

import org.junit.Test;

public class TransitEntryTest {

    private final TransitEntry transitEntry = new TransitEntry();
    private int serialNumber;


    @Test
    public void testIfSerialNumberGetCountUpByOne() {

        transitEntry.countUp();
    }
}
