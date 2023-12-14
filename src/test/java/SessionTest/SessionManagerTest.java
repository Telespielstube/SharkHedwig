package SessionTest;

import Message.Contract.Contract;
import Message.Identification.AbstractIdentification;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Message.MessageFlag;
import Message.MessageHandler;
import Session.ISessionManager;
import Session.SessionManager;
import Session.SessionState;
import Setup.Component;
import Setup.DeviceState;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SessionManagerTest {

  //  private final ISessionManager sessionManager = new SessionManager(MessageHandler messageHandler, SharkPKIComponent sharkPKIComponent, ASAPPeer peer);
    private final Challenge challenge = new Challenge(UUID.randomUUID(), System.currentTimeMillis());
    private final Response response = new Response(UUID.randomUUID(), System.currentTimeMillis());
    private final Contract contract = new Contract();
    private SharkPKIComponent sharkPKIComponent;
    private ASAPPeer peer;
    private MessageHandler messageHandler = null;
    private final String sender = "Olli";



    @Test
    public void testIfChallengeObjectIsPutIntoTheRightSession() {
        ISessionManager sessionManager = new SessionManager(messageHandler, peer, sharkPKIComponent);
        DeviceState deviceState = DeviceState.Transferor.isActive();
        SessionState sessionState = SessionState.Identification;
        assertTrue(sessionManager.handleSession(challenge, sender, sessionState, deviceState));
    }

    @Test
    public void testIfResponseObjectIsPutIntoTheRightSession() {
      ISessionManager sessionManager = new SessionManager(messageHandler, peer, sharkPKIComponent);
        DeviceState deviceState = DeviceState.Transferor.isActive();
        SessionState sessionState = SessionState.Identification;
        assertTrue(sessionManager.handleSession(response, sender, sessionState, deviceState));
    }

    @Test
    public void testIfContractObjectIsPutIntoTheRightSession() {
      ISessionManager sessionManager = new SessionManager(messageHandler, peer, sharkPKIComponent);
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
