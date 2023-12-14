package Message.Identification;

import Message.MessageFlag;

import java.security.SecureRandom;
import java.util.UUID;

public class Response extends AbstractIdentification {

    private byte[] encryptedNumber;
    private byte[] decryptedNumber;

    public Response(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Response(UUID uuid, byte[] encryptedNumber, byte[] decryptedNumber, MessageFlag messageFlag, long timestamp) {
        this.uuid = uuid;
        this.encryptedNumber = encryptedNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.decryptedNumber = decryptedNumber;
    }

    public byte[] getEncryptedNumber() {
        return this.encryptedNumber;
    }
    public void setEncryptedNumber(byte[] responseNumber) {
        this.encryptedNumber = responseNumber;
    }

    public byte[] getDecryptedNumber() {
        return this.decryptedNumber;
    }
    public void setDecryptedNumber(byte[] responseNumber) {
        this.decryptedNumber = decryptedNumber;
    }
    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }
}
