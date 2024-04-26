package DeliveryContract.State;

import DeliveryContract.DeliveryContract;

public class DeliveryContractState implements ContractState {
    private final DeliveryContract deliveryContract;

    public DeliveryContractState(DeliveryContract deliveryContract) {
        this.deliveryContract = deliveryContract;
    }

    @Override
    public boolean isCreated() {
        return false;
    }
}
