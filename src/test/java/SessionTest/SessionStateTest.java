package SessionTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SessionStateTest {


    @Test
    public void testIfNexSessioneIsRequest() {
        assertEquals(SessionState_tmp.REQUEST, SessionState_tmp.NO_SESSION.nextState());
    }

    @Test
    public void testIfNextSessionIsActive() {
        assertEquals(SessionState_tmp.CONTRACT, SessionState_tmp.REQUEST.nextState());
    }

    @Test
    public void testIfResetNoSessionIsNoSession() {
        assertEquals(SessionState_tmp.NO_SESSION, SessionState_tmp.NO_SESSION.resetState());
    }

    @Test
    public void testIResetContractSessionIsNoSession() {
        assertEquals(SessionState_tmp.NO_SESSION, SessionState_tmp.CONTRACT.resetState());
    }

    @Test
    public void testIfResetContractSessionIsNotAuthentificationnSession() {
        assertNotEquals(SessionState_tmp.REQUEST, SessionState_tmp.CONTRACT.resetState());
    }
}
