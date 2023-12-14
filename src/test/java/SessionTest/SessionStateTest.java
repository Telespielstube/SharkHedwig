package SessionTest;

import Session.SessionState;
import Setup.DeviceState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionStateTest {

    @Test
    public void testIfStateVariableProceedsToNextState() {
        SessionState state = SessionState.NoSession.currentState();

        //state.nextState();
        assertEquals("Identification", state.nextState().toString());
    }
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
        assertEquals(SessionState.Contract.currentState(), state);
    }

    @Test
    public void checkIfNextStateAfterHandoverIsNoState() {
        SessionState state = SessionState.Contract.nextState();
        assertEquals(SessionState.NoSession.currentState(), state);
    }
}
