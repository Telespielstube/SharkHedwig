package SessionTest;

import Session.SessionState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionStateTest {

    @Test
    public void testIfNoStateIsActive() {
        SessionState state = SessionState.NoSession.getState();
        assertEquals(SessionState.NoSession.getState(), state);
    }

    @Test
    public void checkIfNextStateAfterNoStateIsIdentificationState() {
        SessionState state = SessionState.NoSession.nextState();
        assertEquals(SessionState.Identification.getState(), state);
    }

    @Test
    public void checkIfNextStateAfterIdentificationIsHandoverState() {
        SessionState state = SessionState.Request.nextState();
        assertEquals(SessionState.Handover.getState(), state);
    }

    @Test
    public void checkIfNextStateAfterHandoverIsNoState() {
        SessionState state = SessionState.Handover.nextState();
        assertEquals(SessionState.NoSession.getState(), state);
    }
}
