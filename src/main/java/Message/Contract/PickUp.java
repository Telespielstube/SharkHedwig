package Message.Contract;

import Location.Location;
import Message.MessageFlag;

import java.util.UUID;

public class PickUp extends AbstractContract {

    private Location pickUpLocation;
    public PickUp() {}

    public PickUp(UUID uuid, MessageFlag messageFlag, long timestamp, Location pickUpLocation) {
        this.messageFlag = messageFlag;
        this.pickUpLocation = pickUpLocation;
    }

    public int getMessageFlag() {
        return this.messageFlag.getFlag();
    }

    public void setMessageFlag(MessageFlag messageFlag) {
        this.messageFlag = messageFlag;
    }

    public Location getPickUpLocation() {
        return this.pickUpLocation;
    }

    public void setPickUpLocation(Location pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
}
