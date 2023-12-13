package Session;


import HedwigUI.DeviceState;
import Message.IMessage;
import Message.Advertisement;
import Message.IMessageHandler;
import Message.Identification.AbstractIdentification;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Message.MessageFlag;
import Session.IdentificationSession.IIdentificationSession;
import Session.IdentificationSession.IdentificationSession;

public class SessionManager implements ISessionManager {

    private SessionState state;
    private DeviceState deviceState;
    private SessionState nextState;
    private IIdentificationSession identificationSession;
    private IMessageHandler messageHandler;
    public SessionManager() {}

    public <T> boolean handleSession(T message, String sender, IMessageHandler messageHandler, SessionState state, DeviceState deviceState) {
        this.state = state;
        this.nextState = state;
        this.deviceState = deviceState;
        this.messageHandler = messageHandler;

        if ( (message instanceof IMessage) && (this.state.equals(SessionState.NoSession) ) && (this.deviceState.equals(DeviceState.Transferor)) ) {
            identificationSession = (IIdentificationSession) new IdentificationSession(sender, messageHandler);
            identificationSession.initializeSession();
            this.nextState.nextState();
        }
//        if ( (message instanceof AbstractIdentification) && (this.state.equals(SessionState.Identification) ) {
//            new Challenge(((AbstractIdentification) message).getUuid(), ((Challenge) message).getChallengeNumber(),
//                        ((Challenge) message).getMessageFlag(), ((Challenge) message).getTimestamp());
//            } if ( ((AbstractIdentification) message).getMessageFlag().equals(MessageFlag.Response.getFlag() ) ) {
//                new Response(((AbstractIdentification) message).getUuid(), ((Response) message).getResponseNumber(),
//                        ((Response) message).getMessageFlag(), ((Response) message).getTimestamp());
//            }

//        else if (message instanceof AbstractRequest) {
//            if (((AbstractRequest) message).getMessageFlag() == REQUEST_MESSAGE_FLAG) {
//                new Request();
//            } else {
//                new RequestReply();
//            }
//        }
//        else if (message instanceof AbstractContract) {
//            if (((AbstractContract) message).getMessageFlag() == CONTRACT_MESSAGE_FLAG) {
//                new Contract();
//            } else {
//                new PickUp();
//            }
//        }
//        else if (message instanceof IMessage){
//            if (((IMessage) message).getMessageFlag() == ACK_MESSAGE_FLAG) {
//                new AckMessage();
//            }
//        } else {
//            return false;
//        }
         return true;
    }
}
