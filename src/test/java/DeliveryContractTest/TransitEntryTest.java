package DeliveryContractTest;

import DeliveryContract.TransitEntry;
import Location.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class TransitEntryTest {

    private TransitEntry transitEntry;
    private int serialNumber;
    private UUID packageUUID;
    private String transferor;
    private String transferee;
    private Location handoverLocation;
    private long timestamp;
    private byte[] digitalSignature;

    @Before
    public void setup() {
        transitEntry = new TransitEntry(2, null, "Alice", "Bob", new Location(80.0,90.0), 45345345, null );
    }

    @Test
    public void testIfSerialNumberGetCountUpByOne() {
        System.out.println(transitEntry.countUp());
    }
}