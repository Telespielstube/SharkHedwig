package Message.Identification;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import static Misc.Constants.CHALLENGE_MESSAGE_FLAG;


public class Challenge extends AbstractIdentification {

    private SecureRandom challengeNumber;
    public Challenge(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Challenge(UUID uuid, SecureRandom challengeNumber, int messageFlag, long timestamp) {
        this.uuid = uuid;
        this.challengeNumber = challengeNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    /**
     * Generates are cryptographically strong random number. The getInstanceStrong() uses the strongest crypto
     * algorithms available on the system.
     */
    public void generateChallengeNumber() {
        try {
            this.challengeNumber = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.challengeNumber.nextBytes(new byte[128]);
    }

    public SecureRandom getChallengeNumber() {
        return this.challengeNumber;
    }

    public void setChallengeNumber(SecureRandom challengeNumber) {
        this.challengeNumber = challengeNumber;
    }


    public int getMessageFlag() {
        return this.messageFlag = CHALLENGE_MESSAGE_FLAG;
    }

    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
    }
}
