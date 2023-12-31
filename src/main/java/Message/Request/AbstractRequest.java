package Message.Request;

import java.security.SecureRandom;
import java.util.UUID;

import Message.IMessage;
import Location.Location;
import Message.MessageFlag;

public abstract class AbstractRequest implements IMessage {
    protected UUID uuid;
    protected SecureRandom challengeNumber;
    protected MessageFlag messageFlag;
    protected long timestamp;
    protected Location currentLocation;

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

    public abstract MessageFlag getMessageFlag();
    public abstract void setMessageFlag(MessageFlag messageFlag);

    @Override
    public Object getContent() {
        return this;
    }
}

