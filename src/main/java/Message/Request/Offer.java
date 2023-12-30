package Message.Request;

import Location.Location;
import Message.MessageFlag;

import java.util.UUID;

public class Offer extends AbstractRequest {

    private double flightRange = 0.0;
    private double maxFreightWeight = 0.0;

    public Offer() {}
    public Offer(UUID uuid, MessageFlag messageFlag, long timestamp, double flightRange, double maxFreightWeight, Location currentLocation) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.flightRange = flightRange;
        this.maxFreightWeight = maxFreightWeight;
        this.currentLocation = currentLocation;
    }

    public MessageFlag getMessageFlag() {
        return this.messageFlag;
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    @Override
    public Object getContent() {
        return null;
    }

    public double getFlightRange() {
        return this.flightRange;
    }

    public void setFlightRange(double flightRange) {
        this.flightRange = flightRange;
    }

    public double getMaxFreightWeight() {
        return this.maxFreightWeight;
    }

    public void setMaxFreightWeight(double maxFreightWeight) {
        this.maxFreightWeight = maxFreightWeight;
    }
}
