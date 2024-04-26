package Message.Request;

import Location.Location;
import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class OfferReply extends Message {

    private double packageWeight;
    private Location packageDestination;

    public OfferReply(UUID uuid, MessageFlag messageFlag, long timestamp, double packageWeight, Location packageDestination) {
        super(uuid, messageFlag, timestamp);
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
