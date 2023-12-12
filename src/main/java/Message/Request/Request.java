package Message.Request;

import com.sun.xml.internal.ws.wsdl.writer.document.Message;

import java.security.SecureRandom;
import java.util.UUID;

import static Misc.Constants.CHALLENGE_MESSAGE_FLAG;

public abstract class Request implements Message {
    private UUID uuid;
    private SecureRandom challengeNumber;
    private int messageFlag = CHALLENGE_MESSAGE_FLAG;;
    private long timestamp;

    public UUID createUUID() {
        return UUID.randomUUID();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFlag() {
        return this.messageFlag;
    }

    public void setFlag(int flag) {
        this.messageFlag = flag;
    }
}

