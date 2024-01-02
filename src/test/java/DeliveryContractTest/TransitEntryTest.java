package DeliveryContractTest;

import DeliveryContract.TransitEntry;
import Location.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransitEntryTest {

    private static TransitEntry transitEntry;
    private int serialNumber;
    private UUID packageUUID;
    private String transferor;
    private String transferee;
    private Location handoverLocation;
    private long timestamp;
    private byte[] digitalSignature;

    @BeforeAll
    public static void setup() {
        transitEntry = new TransitEntry(2, null, "Alice", "Bob", new Location(80.0,90.0), 45345345, null );
    }

    @Test
    public void testIfSerialNumberGetCountUpByOne() {
        System.out.println(transitEntry.getSerialNumber());
        assertEquals(3, transitEntry.getSerialNumber());
    }
}
