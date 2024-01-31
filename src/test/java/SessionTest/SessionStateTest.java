package SessionTest;

import Session.SessionState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SessionStateTest {

    @Test
    public void testIfNextSessionIsAuthentification() {
        assertEquals(SessionState.AUTHENTIFICATION, SessionState.NO_SESSION.nextState());
    }

    @Test
    public void testIfNexSessioneIsRequest() {
        assertEquals(SessionState.REQUEST, SessionState.AUTHENTIFICATION.nextState());
    }

    @Test
    public void testIfNextSessionIsActive() {
        assertEquals(SessionState.CONTRACT, SessionState.REQUEST.nextState());
    }

    @Test
    public void testIfResetNoSessionIsNoSession() {
        assertEquals(SessionState.NO_SESSION, SessionState.NO_SESSION.resetState());
    }

    @Test
    public void testIResetContractSessionIsNoSession() {
        assertEquals(SessionState.NO_SESSION, SessionState.CONTRACT.resetState());
    }

    @Test
    public void testIResetContractSessionIsNotAuthentificationnSession() {
        assertNotEquals(SessionState.AUTHENTIFICATION, SessionState.CONTRACT.resetState());
    }
}
