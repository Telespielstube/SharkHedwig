package SessionTest;

import Session.SessionState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class IdentificationStateTest {

    @Test
    public void testIfStateVariableProceedsToNextState() {
        SessionState state = SessionState.NOSESSION.resetState();

        //state.nextState();
        assertEquals("Identification", state.nextState().toString());
    }
    @Test
    public void testIfNoStateIsActive() {
        SessionState state = SessionState.NOSESSION.resetState();
        assertEquals(SessionState.NOSESSION.resetState(), state);
    }

    @Test
    public void checkIfNextStateAfterNoStateIsIdentificationState() {
        SessionState state = SessionState.NOSESSION.nextState();
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
        assertEquals(SessionState.NOSESSION.resetState(), state);
    }
}
