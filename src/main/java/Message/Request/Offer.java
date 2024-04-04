package Message.Request;

import Location.Location;
import Message.Message;
import Message.MessageFlag;

import java.util.UUID;

public class Offer extends Message {

    private int flightRange = 0;
    private double maxFreightWeight = 0.0;
    private Location currentLocation;

    public Offer(UUID uuid, MessageFlag messageFlag, long timestamp, int flightRange, double maxFreightWeight, Location currentLocation) {
        super(uuid, messageFlag, timestamp);
        this.flightRange = flightRange;
        this.maxFreightWeight = maxFreightWeight;
        this.currentLocation = currentLocation;
    }

    public int getFlightRange() {
        return this.flightRange;
    }

    public double getMaxFreightWeight() {
        return this.maxFreightWeight;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }
}
