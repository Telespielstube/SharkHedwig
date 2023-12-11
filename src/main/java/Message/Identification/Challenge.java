package Message.Identification;

import static Misc.Constants.CHALLENGE_MESSAGE_FLAG;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;


public class Challenge extends Identification {

    private UUID uuid;
    private SecureRandom challengeNumber;
    private int messageFlag = CHALLENGE_MESSAGE_FLAG;;
    private long timestamp;

    public Challenge() {}
    public Challenge(UUID uuid, SecureRandom challengeNumber, long timestamp) {
        this.uuid = uuid;
        this.challengeNumber = challengeNumber;
        this.timestamp = timestamp;
    }

    /**
     * Generates are cryptographically strong random number. The getInstanceStrong() uses the strongest crypto
     * algorithms available on the system.
     */
    private void generateSecureNumber() {
        try {
            this.challengeNumber = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.challengeNumber.nextBytes(new byte[128]);
    }

    @Override
    public int getFlag() {
        return this.messageFlag;
    }
}
