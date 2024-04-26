package DeliveryContract.State;

import DeliveryContract.DeliveryContract;

public class TransitRecordState implements ContractState {
    private final DeliveryContract deliveryContract;

    public TransitRecordState(DeliveryContract deliveryContract) {
        this.deliveryContract = deliveryContract;
    }

    @Override
    public boolean isCreated() {
        return false;
    }
}
