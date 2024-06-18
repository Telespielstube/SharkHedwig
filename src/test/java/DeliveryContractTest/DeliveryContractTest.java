package DeliveryContractTest;

import DeliveryContract.DeliveryContract;
import DeliveryContract.ShippingLabel;
import DeliveryContract.TransitEntry;
import DeliveryContract.TransitRecord;
import Location.Location;
import java.util.UUID;

import SetupTest.TestConstant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeliveryContractTest {

    private static DeliveryContract deliveryContract;
    private static ShippingLabel shippingLabel;
    private static TransitRecord transitRecord;

    @Test
    public void testIfDeliveryContractGetsCloned() {
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PEER_NAME.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null, null));
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        DeliveryContract deliveryContract1 = (DeliveryContract)deliveryContract.clone();

        assertEquals(deliveryContract1.get().getShippingLabel().getUUID(), deliveryContract.get().getShippingLabel().getUUID());
    }
    @Test
    public void getShippingLabelAndTransitRecord() {
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PEER_NAME.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null, null));
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        System.out.println(deliveryContract.getClass());
        System.out.println(deliveryContract.get());
        assertNotNull(deliveryContract.getShippingLabel());
        assertNotNull(deliveryContract.getTransitRecord());
    }

    @Test
    public void deliveryContractIsCreatedWithoutAttributes() {
        shippingLabel = new ShippingLabel(null,null,null, null,
                null, null, null, 0.0);
        transitRecord = new TransitRecord();
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        assertNotNull(deliveryContract);
        assertTrue(deliveryContract.isCreated());
        assertNotNull(transitRecord.getClass());
    }

    @Test
    public void deliveryContractThrowsExceptionWhenTransitRecordIsCreatedButShippingLabelIsNotPresent() {
        shippingLabel = null;
        transitRecord = new TransitRecord();
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        assertNotNull(transitRecord.get());
        assertThrowsExactly(NullPointerException.class, () -> shippingLabel.isCreated());
    }
    @Test
    public void returnTrueIfContractSetMethodIsCalled() {
        assertNotNull(deliveryContract = new DeliveryContract(shippingLabel, transitRecord));
        assertTrue(deliveryContract.isCreated());
    }

    @Test
    public void ShippingLabelAndTransitRecordObjectsAreAccessible() {
        transitRecord = new TransitRecord();
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        transitRecord.addEntry(new TransitEntry(2, null, "Alice", "Bobby", new Location(80.0,90.0), 45345345, null, null ));
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        System.out.println(deliveryContract.getShippingLabel());
        assertNotNull(deliveryContract.getShippingLabel());
        System.out.println(deliveryContract.getTransitRecord().getAllEntries());
        assertNotNull(deliveryContract.getTransitRecord().getAllEntries());
        System.out.println(deliveryContract.get());
        assertNotNull(deliveryContract.get());
    }

    @Test
    public void getTransitRecordThrowsExceptionCauseNoObjectIsPresent() {
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        assertThrows(NullPointerException.class,
                ()-> deliveryContract.getTransitRecord());
        assertThrows(NullPointerException.class,
                ()-> deliveryContract.getShippingLabel());
    }

    @Test
    public void testIfTransitRecordEntryIsReturnedAndNotNull() {
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(2, null, "Alice", "Bobby", new Location(80.0,90.0), 45345345, null, null ));
        System.out.println(transitRecord.toString());
        assertNotNull(transitRecord);

    }

    @Test
    public void testIfShippingLabelIsReturned() {
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        assertNotNull(shippingLabel.getUUID());
        assertEquals("Bob", shippingLabel.getRecipient());
        assertNotNull(shippingLabel);
    }

    @Test
    public void printOutDeliveryContract() {
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        transitRecord = new TransitRecord();
        transitRecord.addEntry(new TransitEntry(0, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (57.5654645, 77.345345), 56563456, null, null));
        transitRecord.addEntry(new TransitEntry(2, null, TestConstant.PEER_NAME.name(), "Peter", new Location
                (55.5654645, 76.345345), 54863456, null, null));
        transitRecord.addEntry(new TransitEntry(4, null, TestConstant.PEER_NAME.name(), "Bob", new Location
                (55.5654645, 76.345345), 54566456, null, null));
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        System.out.println(deliveryContract);
    }
}
