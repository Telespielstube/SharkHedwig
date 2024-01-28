package SessionTest;

import Session.SessionState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class IdentificationStateTest {

    @Test
    public void testIfStateVariableProceedsToNextState() {
        SessionState state = SessionState.NO_SESSION.resetState();
        assertEquals("IDENTIFICATION", state.nextState().toString());
    }
    @Test
    public void testIfNoStateIsActive() {
        SessionState state = SessionState.NO_SESSION.resetState();
        assertEquals(SessionState.NO_SESSION.resetState(), state);
    }

    @Test
    public void checkIfNextStateAfterNoStateIsIdentificationState() {
        SessionState state = SessionState.NO_SESSION.nextState();
        assertEquals(SessionState.IDENTIFICATION, state);
    }

    @Test
    public void checkIfNextStateAfterRequestIsContractState() {
        SessionState state = SessionState.REQUEST.nextState();
        assertEquals(SessionState.CONTRACT, state);
    }

    @Test
    public void checkIfNextStateAfterHandoverIsNoState() {
        SessionState state = SessionState.CONTRACT.nextState();
        assertEquals(SessionState.NO_SESSION.resetState(), state);
    }
}
