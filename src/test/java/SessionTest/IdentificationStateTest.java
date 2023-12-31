package SessionTest;

import Session.SessionState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class IdentificationStateTest {

    @Test
    public void testIfStateVariableProceedsToNextState() {
        SessionState state = SessionState.NoSession.resetSessionState();

        //state.nextState();
        assertEquals("Identification", state.nextState().toString());
    }
    @Test
    public void testIfNoStateIsActive() {
        SessionState state = SessionState.NoSession.resetSessionState();
        assertEquals(SessionState.NoSession.resetSessionState(), state);
    }

    @Test
    public void checkIfNextStateAfterNoStateIsIdentificationState() {
        SessionState state = SessionState.NoSession.nextState();
        assertEquals(SessionState.Identification.nextState(), state);
    }

    @Test
    public void checkIfNextStateAfterIdentificationIsHandoverState() {
        SessionState state = SessionState.Request.nextState();
        assertEquals(SessionState.Contract.resetSessionState(), state);
    }

    @Test
    public void checkIfNextStateAfterHandoverIsNoState() {
        SessionState state = SessionState.Contract.nextState();
        assertEquals(SessionState.NoSession.resetSessionState(), state);
    }
}
