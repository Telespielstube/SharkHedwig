package Message.Identification;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.UUID;

public class Challenge extends Identification {

    private UUID uuid;
    private SecureRandom challengeNumber;
    private String messageFlag;
    private long timestamp;

    public Challenge() {}
    public Challenge(UUID uuid, SecureRandom challengeNumber, String messageFlag, long timestamp) {
        this.uuid = uuid;
        this.challengeNumber = challengeNumber;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
    }

    public Challenge(UUID uuid, String messageFlag, long timestamp) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
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
}
