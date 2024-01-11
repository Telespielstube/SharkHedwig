package SessionTest;

import Session.SessionState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SessionStateTest {

    @Test
    public void testIfNextSessionIsIdentification() {
        assertEquals(SessionState.Identification, SessionState.NoSession.nextState());
    }

    @Test
    public void testIfNexSessioneIsRequest() {
        assertEquals(SessionState.Request, SessionState.Identification.nextState());
    }

    @Test
    public void testIfNextSessionIsActive() {
        assertEquals(SessionState.Contract, SessionState.Request.nextState());
    }

    @Test
    public void testIfResetNoSessionIsNoSession() {
        assertEquals(SessionState.NoSession, SessionState.NoSession.resetState());
    }

    @Test
    public void testIResetContractSessionIsNoSession() {
        assertEquals(SessionState.NoSession, SessionState.Contract.resetState());
    }

    @Test
    public void testIResetContractSessionIsNotIdentificationSession() {
        assertNotEquals(SessionState.Identification, SessionState.Contract.resetState());
    }
}
