package DeliveryContract;

public enum ContractState {
    CREATED(true);

    private boolean state;
    ContractState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }
}
