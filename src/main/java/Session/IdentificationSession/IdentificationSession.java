package Session.IdentificationSession;

import java.math.BigInteger;
import java.security.SecureRandom;

import Message.Identification.AbstractIdentification;
import Message.Identification.AckMessage;
import Message.Identification.Response;
import Misc.Utilities;
import Setup.Channel;
import Message.IMessageHandler;
import Message.Identification.Challenge;
import Message.MessageFlag;
import Setup.Constant;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;

public class IdentificationSession implements IIdentificationSession {

    private SharkPKIComponent sharkPKIComponent;
    private ASAPPeer peer;
    private String sender;
    private IMessageHandler messageHandler;
    private Challenge challenge;
    private byte[] challengeNumber;

    public IdentificationSession(String sender, SharkPKIComponent sharkPKIComponent) {
        this.sender = sender;
        this.sharkPKIComponent = sharkPKIComponent;
    }

    @Override
    public Challenge initSession() {
        return new Challenge(this.challenge.createUUID(), encryptRandomNumber() , MessageFlag.Challenge, Utilities.createTimestamp());

    }

    public boolean compareNumber() {

    }

    public void processMessage(Object message) {
        if (((Response) message).getMessageFlag().equals(MessageFlag.Response.getFlag())) {
            Response response = (Response) message;
            response.getResponseNumber();
        }

        if (((AckMessage) message).getMessageFlag().equals(MessageFlag.Ack.getFlag())) {

    }
    }

    public byte[] generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger bigInt = BigInteger.valueOf(secureRandom.nextInt());
        this.challengeNumber =  bigInt.toByteArray();
        return this.challengeNumber;
    }

    public byte[] encryptRandomNumber() {
        byte[] encrypted = new byte[0];
        try {
            encrypted = ASAPCryptoAlgorithms.produceEncryptedMessagePackage(generateRandomNumber(), sender, this.sharkPKIComponent );
        } catch (ASAPSecurityException e) {
            e.printStackTrace();
        }
        return encrypted;
    }
}
