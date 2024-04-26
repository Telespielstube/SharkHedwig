package DeliveryContract.State;

/**
 * Enum class that represents the states of the de;iver contract object.
 */
public interface ContractState {

    /**
     * Returns the state of the delivery contract and its two components.
     *
     * @return    boolean value true if it is created or false if not.
     */
    boolean isCreated();

}
