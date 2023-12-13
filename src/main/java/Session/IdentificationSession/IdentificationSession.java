package Session.IdentificationSession;

import java.security.SecureRandom;

import Message.MessageHandler;

public class IdentificationSession implements IIdentificationSession {

    private String sender;
    public IdentificationSession() {}

    @Override
    public void initializeSession(String sender) {
        this.sender = sender;
    }


    public int generateRandomNumber() {
        SecureRandom random = new SecureRandom();
        return random.nextInt();
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }
}
