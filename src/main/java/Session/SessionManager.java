package Session;


import Message.AckMessage;
import Message.Contract.AbstractContract;
import Message.Contract.Contract;
import Message.Contract.PickUp;
import Message.IMessage;
import Message.Identification.AbstractIdentification;
import Message.Identification.Challenge;
import Message.Identification.Response;
import Message.Request.AbstractRequest;
import Message.Request.Request;
import Message.Request.RequestReply;

import java.io.IOException;

import static Misc.Constants.*;

public class SessionManager implements ISessionManager {

    public SessionManager() {}

    public <T> boolean handleSession(T message) {
        if (message instanceof AbstractIdentification) {

            if (((AbstractIdentification) message).getMessageFlag() == CHALLENGE_MESSAGE_FLAG) {
                new Challenge(((AbstractIdentification) message).getUuid(), ((Challenge) message).getChallengeNumber(),
                        ((Challenge) message).getMessageFlag(), ((Challenge) message).getTimestamp());
            } else {
                new Response(((AbstractIdentification) message).getUuid(), ((Response) message).getResponseNumber(),
                        ((Response) message).getMessageFlag(), ((Response) message).getTimestamp());
            }
        }
        else if (message instanceof AbstractRequest) {
            if (((AbstractRequest) message).getMessageFlag() == REQUEST_MESSAGE_FLAG) {
                new Request();
            } else {
                new RequestReply();
            }
        }
        else if (message instanceof AbstractContract) {
            if (((AbstractContract) message).getMessageFlag() == CONTRACT_MESSAGE_FLAG) {
                new Contract();
            } else {
                new PickUp();
            }
        }
        else if (message instanceof IMessage){
            if (((IMessage) message).getMessageFlag() == ACK_MESSAGE_FLAG) {
                new AckMessage();
            }
        } else {
            return false;
        }
        return true;
    }
}
