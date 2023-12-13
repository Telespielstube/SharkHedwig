package Session;


import HedwigUI.DeviceState;
import Message.IMessage;
import Message.Advertisement;
import Message.MessageFlag;
import Session.IdentificationSession.IdentificationSession;

public class SessionManager implements ISessionManager {

    private SessionState state;
    private DeviceState deviceState;
    private IdentificationSession identificationSession;
    public SessionManager() {}

    public <T> boolean handleSession(T message, String sender, SessionState state, DeviceState deviceState) {
        this.state = state;
        this.deviceState = deviceState;

        if ( (message instanceof IMessage) && (this.state.equals(SessionState.NoSession) ) &&
                (deviceState.equals(DeviceState.Transferor) ) &&
                 ((IMessage) message).getMessageFlag() == MessageFlag.) {
            new Advertisement(((IMessage) message).getUuid(), ((IMessage) message).getMessageFlag(), ((IMessage) message).getTimestamp());
        }
//        if ( (message instanceof AbstractIdentification) && ( ((AbstractIdentification) message).getMessageFlag() == CHALLENGE_MESSAGE_FLAG) ) {
//            new Challenge(((AbstractIdentification) message).getUuid(), ((Challenge) message).getChallengeNumber(),
//                        ((Challenge) message).getMessageFlag(), ((Challenge) message).getTimestamp());
//            } if ( ((AbstractIdentification) message).getMessageFlag() == RESPONSE_MESSAGE_FLAG ) {
//                new Response(((AbstractIdentification) message).getUuid(), ((Response) message).getResponseNumber(),
//                        ((Response) message).getMessageFlag(), ((Response) message).getTimestamp());
//            }
//        }
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
