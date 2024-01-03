package DeliveryContractTest;

import DeliveryContract.*;
import Location.Location;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeliveryContractTest {

    private static DeliveryContract deliveryContract;
    private static ShippingLabel shippingLabel;
    private static TransitRecord transitRecord;


    @Test
    public void deliveryContractIsCreatedWithoutAttributes() {
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        shippingLabel = new ShippingLabel();
        transitRecord = new TransitRecord();
        assertNotNull(deliveryContract);
        assertFalse(deliveryContract.getContractSent());
        assertNotNull(transitRecord.getClass());
    }

    @Test
    public void deliveryContractThrowsExceptionWhenShippingAndTransitRecordIsNotPresent() {
        deliveryContract = new DeliveryContract(shippingLabel, transitRecord);
        shippingLabel = new ShippingLabel();
        transitRecord = new TransitRecord();
        assertFalse(shippingLabel.getIsCreated());
        assertFalse(transitRecord.getIsCreated());
    }
    @Test
    public void returnTrueIfContractSetMethodIsCalled() {
        assertNotNull(deliveryContract = new DeliveryContract(shippingLabel, transitRecord));
        deliveryContract.setContractSent(true);
        assertTrue(deliveryContract.getContractSent());
    }

    @Test
    public void returnFalseIfContractSetMethodIsNotCalled() {
        assertNotNull(deliveryContract = new DeliveryContract(shippingLabel, transitRecord));
        assertFalse(deliveryContract.getContractSent());

    }

    @Test
    public void ShippingLabelAndTransitRecordObjectsAreAccessible() {
        transitRecord = new TransitRecord();
        shippingLabel = new ShippingLabel(UUID.randomUUID(), "Alice", "HTW-Berlin",
                new Location(80.67, 90.56), "Bob", "Ostbahnhof",
                new Location(52.5105, 13.4346), 1.2);
        transitRecord.addEntry(new TransitEntry(2, null, "Alice", "Bobby", new Location(80.0,90.0), 45345345, null ));
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
        transitRecord.addEntry(new TransitEntry(2, null, "Alice", "Bobby", new Location(80.0,90.0), 45345345, null ));
        System.out.println(transitRecord.toString());
        assertNotNull(transitRecord);
        assertEquals(1, transitRecord.getTransitRecordSize());

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
}
