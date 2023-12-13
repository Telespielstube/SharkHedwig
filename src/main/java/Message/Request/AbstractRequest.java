package Message.Request;

import java.security.SecureRandom;
import java.util.UUID;

import Message.IMessage;
import Location.Location;
import static Misc.Constants.CHALLENGE_MESSAGE_FLAG;
import static Misc.Constants.RESPONSE_MESSAGE_FLAG;

public abstract class AbstractRequest implements IMessage {
    protected UUID uuid;
    protected SecureRandom challengeNumber;
    protected int messageFlag = 0;
    protected long timestamp;
    protected Location currentLocation;

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

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public abstract int getMessageFlag();
    public abstract void setMessageFlag(int messageFlag);

}
