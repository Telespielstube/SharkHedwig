package SessionTest;

import HedwigUI.DeviceState;
import Message.AckMessage;
import Message.Contract.Contract;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Session.ISessionManager;
import Session.SessionManager;
import Session.SessionState;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class SessionManagerTest {

    private final ISessionManager sessionManager = new SessionManager();
    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());
    private final Response response = new Response(UUID.randomUUID(), System.currentTimeMillis());
    private final Contract contract = new Contract();
    private final AckMessage ackMessage = new AckMessage();
    private final String sender = "Olli";
    @Test
    public void testIfChallengeObjectIsPutIntoTheRightSession() {
        DeviceState deviceState = DeviceState.Transferor.isActive();
        SessionState sessionState = SessionState.Identification;
        assertTrue(sessionManager.handleSession(challenge, sender, sessionState, deviceState));
    }

    @Test
    public void testIfResponseObjectIsPutIntoTheRightSession() {
        DeviceState deviceState = DeviceState.Transferor.isActive();
        SessionState sessionState = SessionState.Identification;
        assertTrue(sessionManager.handleSession(response, sender, sessionState, deviceState));
    }

    @Test
    public void testIfContractObjectIsPutIntoTheRightSession() {
        DeviceState deviceState = DeviceState.Transferor.isActive();
        SessionState sessionState = SessionState.Identification.nextState();
        assertTrue(sessionManager.handleSession(contract, sender, sessionState, deviceState));
    }

//    @Test
//    public void returnTrueIfAckMessageIsTypeOfIMessage() {
//        DeviceState deviceState = DeviceState.Transferor.isActive();
//        SessionState sessionState = SessionState.Identification;
//        assertTrue(sessionManager.handleSession(ackMessage));
//    }
}
