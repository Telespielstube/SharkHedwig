package SessionTest;

import Session.SessionState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class IdentificationStateTest {

    @Test
    public void testIfStateVariableProceedsToNextState() {
        SessionState state = SessionState.NoSession.resetState();

        //state.nextState();
        assertEquals("Identification", state.nextState().toString());
    }
    @Test
    public void testIfNoStateIsActive() {
        SessionState state = SessionState.NoSession.resetState();
        assertEquals(SessionState.NoSession.resetState(), state);
    }

    @Test
    public void checkIfNextStateAfterNoStateIsIdentificationState() {
        SessionState state = SessionState.NoSession.nextState();
        assertEquals(SessionState.Identification, state);
    }

    @Test
    public void checkIfNextStateAfterRequestIsContractState() {
        SessionState state = SessionState.Request.nextState();
        assertEquals(SessionState.Contract, state);
    }

    @Test
    public void checkIfNextStateAfterHandoverIsNoState() {
        SessionState state = SessionState.Contract.nextState();
        assertEquals(SessionState.NoSession.resetState(), state);
    }
}
