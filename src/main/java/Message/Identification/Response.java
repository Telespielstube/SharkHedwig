package Message.Identification;

import Message.MessageFlag;

import java.security.SecureRandom;
import java.util.UUID;

public class Response extends AbstractIdentification {

    private SecureRandom responseNumber;
    public Response(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Response(UUID uuid, SecureRandom responseNumber, MessageFlag messageFlag, long timestamp) {
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
        return this.messageFlag.getFlag();
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }
}
