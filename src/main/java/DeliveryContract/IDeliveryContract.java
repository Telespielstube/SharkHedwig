package DeliveryContract;

import DeliveryContract.TransitRecord;

public interface IDeliveryContract {

    /**
     * Getter for ShippingLabel object.
     *
     * @return   ShippingLabel object.
     */
    ShippingLabel getShippingLabel();

    /**
     * Getter for TransitRecord.
     *
     * @return    TransitRecord object.
     */
    TransitRecord getTransitRecord();
}