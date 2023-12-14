package Session.IdentificationSession;

import java.math.BigInteger;
import java.security.SecureRandom;

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

    public IdentificationSession(String sender, IMessageHandler messageHandler, ASAPPeer peer, SharkPKIComponent sharkPKIComponent) {
        this.sender = sender;
        this.messageHandler = messageHandler;
        this.peer = peer;
        this.sharkPKIComponent = sharkPKIComponent;
    }

    @Override
    public void initializeSession() {
        this.challenge = new Challenge(this.challenge.createUUID(), encryptRandomNumber() , MessageFlag.Challenge, createTimestamp());
        byte[] signedByteObject = this.messageHandler.buildOutgoingMessage(challenge, Channel.Identification.getChannelType(), sender);
        try {
            this.peer.sendASAPMessage(Constant.AppFormat.getAppConstant(), Channel.Identification.getChannelType(), signedByteObject);
        } catch (ASAPException e) {
            throw new RuntimeException(e);
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

    public long createTimestamp() {
        return System.currentTimeMillis();
    }
}
