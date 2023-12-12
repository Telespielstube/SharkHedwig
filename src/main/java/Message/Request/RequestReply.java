package Message.Request;

import Location.Location;

import java.util.UUID;

import static Misc.Constants.REQUEST_REPLY_MESSAGE_FLAG;

public class RequestReply extends AbstractRequest {

    private double actualFreightWeight = 0.0;
    private Location packageDestination = null;

    public RequestReply() {}
    public RequestReply(UUID uuid, int messageFlag, long timestamp, double actualFreightWeight, Location packageDestination ) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.actualFreightWeight = actualFreightWeight;
        this.packageDestination = packageDestination;
    }

    @Override
    public int getMessageFlag() {
        return REQUEST_REPLY_MESSAGE_FLAG;
    }

    @Override
    public void setMessageFlag(int messageFlag) {
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
