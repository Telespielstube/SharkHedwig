package Message.Contract;

import Location.Location;

import java.util.UUID;

public class PickUp extends AbstractContract {

    private Location pickUpLocation;
    public PickUp() {}

    public PickUp(UUID uuid, int messageFlag, long timestamp, Location pickUpLocation) {
        this.messageFlag = messageFlag;
        this.pickUpLocation = pickUpLocation;
    }

    @Override
    public int getMessageFlag() {
        return 0;
    }

    @Override
    public void setMessageFlag(int messageFlag) {

    }

    public Location getPickUpLocation() {
        return this.pickUpLocation;
    }

    public void setPickUpLocation(Location pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
}
