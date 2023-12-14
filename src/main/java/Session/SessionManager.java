package Session;


import Message.Contract.AbstractContract;
import Message.Identification.AbstractIdentification;
import Setup.DeviceState;
import Message.*;
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
    private String sender;
    private T message;

    public SessionManager(MessageHandler messageHandler, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) {
        this.messageHandler = messageHandler;
        this.peer = peer;
        this.sharkPKIComponent = sharkPKIComponent;

    }



    public <T> boolean handleSession(T message, String sender, SessionState state, DeviceState deviceState) {
        this.state = state;
        this.deviceState = deviceState;
        this.sender = sender;

        if (this.deviceState.equals(DeviceState.Transferor.isActive())) {
            handleTransferorMessage(message);
        }
        if (this.deviceState.equals(DeviceState.Transferee.isActive())) {
            transfereeMessage(message);
        }
        return true;
    }

    private <T> void transferorMessage(T message) {
        if (this.state.equals(SessionState.NoSession) && (((IMessage) message).getMessageFlag().equals(MessageFlag.Advertisement.getFlag()))) {
            identificationSession = (IIdentificationSession) new IdentificationSession(sender, this.messageHandler, this.peer, this.sharkPKIComponent);
            identificationSession.initializeSession();
            this.state.nextState();
        }
        if (this.state.equals(SessionState.Identification)) {
            if (((AbstractIdentification) message).getMessageFlag().equals(MessageFlag.Response.getFlag())) {
                identificationSession.parseMessage(message);
            }
            if (((AbstractIdentification) message).getMessageFlag().equals(MessageFlag.Ack.getFlag())) {

            }
            this.state.nextState();
        }
        if (this.state.equals(SessionState.Contract)) {
            if (((AbstractContract) message).getMessageFlag().equals(MessageFlag.Confirmation.getFlag())) {
                identificationSession.parseMessage(message);
            }
        }
    }

    private <T> void transfereeMessage(T message) {
            if (this.state.equals(SessionState.Identification) && ((AbstractIdentification) message).getMessageFlag().equals(MessageFlag.Challenge.getFlag())) {
                identificationSession.parseMessage(message);
            } else {
                identificationSession.parseMessage(message);
            }
            this.state.nextState();
        }
//            else if( ((AbstractIdentification) message).getMessageFlag().equals(MessageFlag.Response.getFlag() ) ) {
//                new Response(((AbstractIdentification) message).getUuid(), ((Response) message).getResponseNumber(),
//                        ((Response) message).getMessageFlag(), ((Response) message).getTimestamp());
//            }
    }
}

