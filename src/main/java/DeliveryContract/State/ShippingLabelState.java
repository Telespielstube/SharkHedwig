package DeliveryContract.State;

import DeliveryContract.DeliveryContract;

public class ShippingLabelState implements ContractState{

    private final DeliveryContract deliveryContract;

    public ShippingLabelState(DeliveryContract deliveryContract) {
        this.deliveryContract = deliveryContract;
    }

    @Override
    public boolean isCreated() {
        return false;
    }
}
