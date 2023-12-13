package Message.Request;

import Location.Location;
import Message.MessageFlag;

import java.util.UUID;

public class RequestReply extends AbstractRequest {

    private double actualFreightWeight = 0.0;
    private Location packageDestination = null;

    public RequestReply() {}
    public RequestReply(UUID uuid, MessageFlag messageFlag, long timestamp, double actualFreightWeight, Location packageDestination ) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.actualFreightWeight = actualFreightWeight;
        this.packageDestination = packageDestination;
    }

    public int getMessageFlag() {
        return this.messageFlag.getFlag();
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    public double getActualFreightWeight() {
        return this.actualFreightWeight;
    }

    public void setActualFreightWeight(double actualFreightWeight) {
        this.actualFreightWeight = actualFreightWeight;
    }

    public Location getPackageDestination() {
        return this.packageDestination = packageDestination;
    }

    public void setPackageDestination(Location packageDestination) {
        this.packageDestination = packageDestination;
    }
}
