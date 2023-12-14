package Message.Identification;

import Message.MessageFlag;

import java.security.SecureRandom;
import java.util.UUID;

public class Response extends AbstractIdentification {

    private byte[] responseNumber;
    private byte[] decryptedNumber;

    public Response(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Response(UUID uuid, byte[] responseNumber, byte[] decryptedNumber, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.responseNumber = responseNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.decryptedNumber = decryptedNumber;
    }

    public byte[] getResponseNumber() {
        return this.responseNumber;
    }
    public void setResponseNumber(byte[] responseNumber) {
        this.responseNumber = responseNumber;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }
}
