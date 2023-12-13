package Session.IdentificationSession;

import java.security.SecureRandom;

import Channel.Channel;
import Message.IMessageHandler;
import Message.Identification.Challenge;
import Message.MessageFlag;

public class IdentificationSession implements IIdentificationSession {

    private String sender;
    private SecureRandom challengeNumber;
    private IMessageHandler messageHandler;
    private Challenge challenge;
    public IdentificationSession(String sender, IMessageHandler messageHandler) {
        this.sender = sender;
        this.messageHandler = messageHandler;
    }

    @Override
    public void initializeSession() {
        this.challenge = new Challenge(this.challenge.createUUID(), generateRandomNumber(), MessageFlag.Challenge, createTimestamp());
        this.messageHandler.buildOutgoingMessage(challenge, Channel.Identification.getChannelType(), sender);
    }


    public int generateRandomNumber() {
        SecureRandom random = new SecureRandom();
        return random.nextInt();
    }

    public long createTimestamp() {
        return System.currentTimeMillis();
    }
}
