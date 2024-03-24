package SessionTest;

import Session.SessionState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SessionStateTest {


    @Test
    public void testIfNexSessioneIsRequest() {
        assertEquals(SessionState.REQUEST, SessionState.NO_SESSION.nextState());
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
    public void testIfResetContractSessionIsNotAuthentificationnSession() {
        assertNotEquals(SessionState.REQUEST, SessionState.CONTRACT.resetState());
    }
}
