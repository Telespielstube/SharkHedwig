package Message.Identification;

import static Misc.Constants.CHALLENGE_MESSAGE_FLAG;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;


public class Challenge extends Identification {

    public Challenge(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Challenge(UUID uuid, SecureRandom challengeNumber, long timestamp) {
        this.uuid = uuid;
        this.challengeNumber = challengeNumber;
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
}
