package Message.Identification;

import java.security.SecureRandom;
import java.util.UUID;

import static Misc.Constants.CHALLENGE_MESSAGE_FLAG;
import static Misc.Constants.RESPONSE_MESSAGE_FLAG;

public class Response extends AbstractIdentification {

    private SecureRandom responseNumber;
    public Response(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Response(UUID uuid, SecureRandom responseNumber, int messageFlag, long timestamp) {
        this.uuid = uuid;
        this.responseNumber = responseNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    public SecureRandom getResponseNumber() {
        return this.responseNumber;
    }
    public void setResponseNumber(SecureRandom responseNumber) {
        this.responseNumber = responseNumber;
    }

    public int getMessageFlag() {
        return this.messageFlag = RESPONSE_MESSAGE_FLAG;
    }

    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
    }
}
