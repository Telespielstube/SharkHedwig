package DeliveryContract;

public enum ContractState {
    CREATED(true);

    private boolean state;

    ContractState(boolean state) {}

    public boolean getState() {
        return state;
    }
}
