package DeliveryContractTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import DeliveryContract.TransitRecord;
import DeliveryContract.TransitEntry;
import Location.Location;
import SetupTest.TestConstant;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class TransitRecordTest {

    private TransitRecord transitRecord;

    @Before
    public void setup() {
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PeerName.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PeerName.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PeerName.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null));
    }

    @Test
    public void testIfLastElementIsReturned() {
        TransitEntry entry = transitRecord.getLastElement();
        assertNotNull(entry);
        assertEquals(4, entry.getSerialNumber());
        assertNotEquals("Bob", entry.getTransferee());
    }

    @Test
    public void testIfTransfereeIsSet() {
        TransitEntry entry = transitRecord.getLastElement();
        entry.setTransferee("Bruce");
        assertEquals("Bruce", entry.getTransferee());
    }

    @Test
    public void testIfVectorGetsCreatedInConstructor() {
        assertEquals(2, transitRecord.getTransitRecordSize());
    }

    @Test
    public void testThatAnTransitEntryObjectGetsAddedToVector() {
        transitRecord.addEntry(new TransitEntry(2, null, "Alice", "Bobby", new Location(80.0,90.0), 45345345, null ));
        assertEquals("Bobby", transitRecord.getAllEntries().lastElement().getTransferee());
        assertEquals(2, transitRecord.getAllEntries().lastElement().getSerialNumber());
    }

    @Test
    public void testIfAllEntriesGetReturned() {
        assertNotNull(transitRecord.getAllEntries());
    }

    @After
    public void clearVector() {
        transitRecord.getAllEntries().clear();
    }

}
