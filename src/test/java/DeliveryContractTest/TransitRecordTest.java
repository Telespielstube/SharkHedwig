package DeliveryContractTest;

import DeliveryContract.TransitRecord;
import DeliveryContract.TransitEntry;
import Location.Location;
import SetupTest.TestConstant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransitRecordTest {

    private static TransitRecord transitRecord;

    @BeforeAll
    public static void setup() {
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PEER_NAME.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null, null));
    }

    @Test
    public void testTransitObject() {

    }
    @Test
    public void testIfLastElementIsReturned() {
        TransitEntry entry = transitRecord.getLastElement();
        assertNotNull(entry);
        assertEquals(4, entry.getSerialNumber());
        assertNotEquals("Bobby", entry.getTransferee());
    }

    @Test
    public void testIfTransfereeIsSet() {
        TransitEntry entry = transitRecord.getLastElement();
        entry.setTransferee("Bruce");
        assertEquals("Bruce", entry.getTransferee());
    }

    @Test
    public void testThatAnTransitEntryObjectGetsAddedToList() {
        transitRecord.addEntry(new TransitEntry(2, null, "Alice", "Bobby", new Location(80.0,90.0), 45345345, null, null ));
        assertEquals("Bobby", transitRecord.getLastElement().getTransferee());
        assertEquals(2, transitRecord.getLastElement().getSerialNumber());
    }

    @Test
    public void testIfAllEntriesGetReturned() {
        System.out.println(transitRecord.getAllEntries());
        assertNotNull(transitRecord.getAllEntries());
    }

    @Test
    public void printAllTransitEntries() {
        System.out.println(transitRecord.toString());
            //    replace("[", "").replace("]", "").replace(",", ""));

    }

    @Test
    public void createNewTransitRecordFromEntyList() {
        System.out.println(transitRecord.getAllEntries());
        transitRecord.getLastElement().setSignatureTransferee("12345".getBytes());
        List<TransitEntry> list = transitRecord.getAllEntries();
        TransitRecord record = new TransitRecord(list);
        System.out.println(record.getAllEntries());
        assertArrayEquals("12345".getBytes(), transitRecord.getLastElement().getSignatureTransferee());
    }

    @AfterAll
    public static void clearList() {
        transitRecord.getAllEntries().clear();
    }
}
