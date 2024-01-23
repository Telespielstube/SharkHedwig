package DeliveryContractTest;

import DeliveryContract.ContractState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContractStateTest {

    @Test
    public void testCurrentContractState() {
        assertTrue(ContractState.CREATED.getState());

    }
}
