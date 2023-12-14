package Session;


import HedwigUI.DeviceState;
import Message.*;
import Message.Contract.AbstractContract;
import Message.Contract.Contract;
import Message.Identification.AbstractIdentification;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Message.Request.AbstractRequest;
import Message.Request.Request;
import Message.Request.RequestReply;
import Session.IdentificationSession.IIdentificationSession;
import Session.IdentificationSession.IdentificationSession;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.pki.SharkPKIComponent;

public class SessionManager implements ISessionManager {

    private SharkPKIComponent sharkPKIComponent;
    private ASAPPeer peer;
    private SessionState state;
    private DeviceState deviceState;
    private SessionState nextState;
    private IIdentificationSession identificationSession;
    private IMessageHandler messageHandler;
    public SessionManager(MessageHandler messageHandler, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) {
        this.messageHandler = messageHandler;
        this.peer = peer;
        this.sharkPKIComponent = sharkPKIComponent;
    }

    public <T> boolean handleSession(T message, String sender, SessionState state, DeviceState deviceState) {
        this.state = state;
        this.deviceState = deviceState;

        if (this.deviceState.equals(DeviceState.Transferor)) {
            if ( (message instanceof IMessage) && (this.state.equals(SessionState.NoSession) ) ) {
                identificationSession = (IIdentificationSession) new IdentificationSession(sender, this.messageHandler, this.peer, this.sharkPKIComponent);
                identificationSession.initializeSession();
                this.state.nextState();
            }
//            else if ( (message instanceof AbstractIdentification) && (this.state.equals(SessionState.Identification)) ) {
//                identificationSession = (IIdentificationSession) new Challenge(((AbstractIdentification) message).getUuid(),
//                        ((Challenge) message).getChallengeNumber(), ((Challenge) message).getMessageFlag(), ((Challenge) message).getTimestamp());
//            }
//            else if( ((AbstractIdentification) message).getMessageFlag().equals(MessageFlag.Response.getFlag() ) ) {
//                new Response(((AbstractIdentification) message).getUuid(), ((Response) message).getResponseNumber(),
//                        ((Response) message).getMessageFlag(), ((Response) message).getTimestamp());
//            }
            }
        return true;
    }
}
