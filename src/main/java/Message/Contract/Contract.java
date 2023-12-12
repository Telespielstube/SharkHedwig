package Message.Contract;

import java.util.UUID;
import DeliveryContract.*;

import static Misc.Constants.REQUEST_MESSAGE_FLAG;

public class Contract extends AbstractContract {

    private ShippingLabel shippingLabel = null;
    private TransitRecord transitRecord = null;

    public Contract() {}
    public Contract(UUID uuid, int messageFlag, long timestamp, ShippingLabel shippingLabel, TransitRecord transitRecord) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.shippingLabel = shippingLabel;
        this.transitRecord = transitRecord;
    }

    @Override
    public int getMessageFlag() {
        return this.messageFlag = REQUEST_MESSAGE_FLAG;
    }

    @Override
    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
    }

    public ShippingLabel getShippingLabel() {
        return this.shippingLabel;
    }

    public void setShippingLabel(ShippingLabel shippingLabel) {
        this.shippingLabel = shippingLabel;
    }

    public TransitRecord getTransitRecord() {
        return this.transitRecord;
    }

    public void setTransitRecord(TransitRecord transitRecord) {
        this.transitRecord = transitRecord;
    }
}
