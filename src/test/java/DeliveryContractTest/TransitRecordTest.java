package DeliveryContractTest;

import DeliveryContract.TransitEntry;
import DeliveryContract.TransitRecord;
import org.junit.After;
import org.junit.Before;

import java.lang.reflect.Parameter;
import java.util.Vector;
import Location.Location;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;


public class TransitRecordTest {

    public TransitRecord transitRecord;
    public Vector<TransitEntry> entryList = null;
    public Vector<TransitEntry> entries = null;

    @Before
    public void setup() {
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(2, null, "Alice", "Bobby", new Location(80.0,90.0), 45345345, null ));
        transitRecord.addEntry(new TransitEntry(6, null, "Clara", "Eric", new Location(87.0,30.0), 45365345, null ));
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
