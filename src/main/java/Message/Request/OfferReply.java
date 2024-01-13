package Message.Request;

import Location.Location;
import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class OfferReply extends Message {

    private double packageWeight = 0.0;
    private Location packageDestination = null;

    public OfferReply(UUID uuid, MessageFlag messageFlag, long timestamp, double packageWeight, Location packageDestination ) {
        super(uuid, messageFlag, timestamp);
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.packageWeight = packageWeight;
        this.packageDestination = packageDestination;
    }

    public double getPackageWeight() {
        return this.packageWeight;
    }

    public Location getPackageDestination() {
        return this.packageDestination;
    }
}
