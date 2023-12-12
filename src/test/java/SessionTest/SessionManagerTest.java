package SessionTest;

import Message.AckMessage;
import Message.Contract.Contract;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Session.ISessionManager;
import Session.SessionManager;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class SessionManagerTest {

    private final ISessionManager sessionManager = new SessionManager();
    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());
    private final Response response = new Response(UUID.randomUUID(), System.currentTimeMillis());
    private final Contract contract = new Contract();
    private final AckMessage ackMessage = new AckMessage();
    @Test
    public void testIfChallengeObjectIsPutIntoTheRightSession() {
        assertTrue(sessionManager.handleSession(challenge));
    }

    @Test
    public void testIfResponseObjectIsPutIntoTheRightSession() {
        assertTrue(sessionManager.handleSession(response));
    }

    @Test
    public void testIfContractObjectIsPutIntoTheRightSession() {
        assertTrue(sessionManager.handleSession(contract));
    }

    @Test
    public void returnTrueIfAckMessageIsTypeOfIMessage() {
        assertTrue(sessionManager.handleSession(ackMessage));
    }
}
