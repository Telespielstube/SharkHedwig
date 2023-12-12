package Message.Request;

import Location.Location;

import java.util.UUID;

import static Misc.Constants.REQUEST_MESSAGE_FLAG;

public class Request extends AbstractRequest {

    private double flightRange = 0.0;
    private double maxFreightWeight = 0.0;

    public Request() {}
    public Request(UUID uuid, int messageFlag, long timestamp, double flightRange, double maxFreightWeight, Location currentLocation) {
        this.uuid = uuid;
        this.messageFlag = messageFlag;
        this.timestamp = timestamp;
        this.flightRange = flightRange;
        this.maxFreightWeight = maxFreightWeight;
        this.currentLocation = currentLocation;
    }

    @Override
    public int getMessageFlag() {
        return this.messageFlag = REQUEST_MESSAGE_FLAG;
    }

    @Override
    public void setMessageFlag(int messageFlag) {
        this.messageFlag = messageFlag;
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
