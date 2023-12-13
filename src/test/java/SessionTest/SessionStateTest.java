package SessionTest;

import Session.SessionState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionStateTest {

    @Test
    public void testIfNoStateIsActive() {
        SessionState state = SessionState.NoSession.currentState();
        assertEquals(SessionState.NoSession.currentState(), state);
    }

    @Test
    public void checkIfNextStateAfterNoStateIsIdentificationState() {
        SessionState state = SessionState.NoSession.nextState();
        assertEquals(SessionState.Identification.nextState(), state);
    }

    @Test
    public void checkIfNextStateAfterIdentificationIsHandoverState() {
        SessionState state = SessionState.Request.nextState();
        assertEquals(SessionState.Handover.currentState(), state);
    }

    @Test
    public void checkIfNextStateAfterHandoverIsNoState() {
        SessionState state = SessionState.Handover.nextState();
        assertEquals(SessionState.NoSession.currentState(), state);
    }
}
