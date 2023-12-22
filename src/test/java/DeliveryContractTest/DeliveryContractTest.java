package DeliveryContractTest;

import DeliveryContract.*;
import Location.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class DeliveryContractTest {

    private DeliveryContract deliveryContract;
    private ShippingLabel shippingLabel;
    private TransitRecord transitRecord;

    @Before
    public void setup() {
        deliveryContract = new DeliveryContract();
        shippingLabel = new ShippingLabel();
        transitRecord = new TransitRecord();
    }

    @Test
    public void testIfDeliveryContractIsCreatedButStillFalse() {
        assertNotNull(deliveryContract);
        assertFalse(DeliveryContract.isCreated);
    }

    @Test
    public void testIfTrueIsReturnedWhenLAbelAndRecordArePassed() {
        assertNotNull(deliveryContract = new DeliveryContract(shippingLabel, transitRecord));
        assertTrue(DeliveryContract.isCreated);
        assertFalse(ShippingLabel.labelCreated);
        assertNotNull(transitRecord.getClass());
    }

    @Test
    public void testIfTransitRecordIsReturnedButFalseBecauseNoEntry() {
        TransitRecord record = deliveryContract.getTransitRecord();
        assertNull(record);
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
    }
}
