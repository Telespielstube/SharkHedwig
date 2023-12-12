package Message.Identification;

import java.security.SecureRandom;
import java.util.UUID;

public class Response extends Identification {

    public Response(UUID uuid, long timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public Response(UUID uuid, SecureRandom challengeNumber, long timestamp) {
        this.uuid = uuid;
        this.challengeNumber = challengeNumber;
        this.timestamp = timestamp;
    }
}

