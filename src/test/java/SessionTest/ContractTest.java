package SessionTest;

import DeliveryContract.TransitEntry;
import DeliveryContract.TransitRecord;
import Location.Location;
import SetupTest.TestConstant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContractTest {

    private static TransitRecord transitRecord;

    @BeforeAll
    public static void setup() {
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PeerName.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PeerName.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PeerName.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null, null));
    }

    @Test
    public void testIfLastElementIsReturned() {
        TransitEntry entry = transitRecord.getLastElement();
        assertNotNull(entry);
        assertEquals(4, entry.getSerialNumber());
        System.out.println(entry.getTransferee());
        assertEquals("Bob", entry.getTransferee());
    }

    @Test
    public void testIfTransfereeIsSet() {
        TransitEntry entry = transitRecord.getLastElement();
        entry.setTransferee("Bruce");
        assertEquals("Bruce", entry.getTransferee());
    }


}
