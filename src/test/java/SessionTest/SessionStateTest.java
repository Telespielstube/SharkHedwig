package SessionTest;

import Session.SessionState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SessionStateTest {

    @Test
    public void testIfNextSessionIsIdentification() {
        assertEquals(SessionState.IDENTIFICATION, SessionState.NOSESSION.nextState());
    }

    @Test
    public void testIfNexSessioneIsRequest() {
        assertEquals(SessionState.REQUEST, SessionState.IDENTIFICATION.nextState());
    }

    @Test
    public void testIfNextSessionIsActive() {
        assertEquals(SessionState.CONTRACT, SessionState.REQUEST.nextState());
    }

    @Test
    public void testIfResetNoSessionIsNoSession() {
        assertEquals(SessionState.NOSESSION, SessionState.NOSESSION.resetState());
    }

    @Test
    public void testIResetContractSessionIsNoSession() {
        assertEquals(SessionState.NOSESSION, SessionState.CONTRACT.resetState());
    }

    @Test
    public void testIResetContractSessionIsNotIdentificationSession() {
        assertNotEquals(SessionState.IDENTIFICATION, SessionState.CONTRACT.resetState());
    }
}
