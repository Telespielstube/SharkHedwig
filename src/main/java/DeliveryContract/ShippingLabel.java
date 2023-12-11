package DeliveryContract;

import java.util.UUID;
import Location.Location;
import static Misc.Constants.PEER_NAME;

/**
 * The shipping label object is a core function of the whole protocoll. The label is created after the user confirmed
 * their entry. The input is immutable that means it cannot be changed after confimration.
 * That is why there are no set methods in this class.
 */
public class ShippingLabel {

    private final UUID packageUUID;
    private String transferorID = null;
    private String sender = null;
    private String origin = null;
    private String recipient = null;
    private double packageWeight = 0.0;
    private String destination = null;
    private Location locationOrigin = null;
    private Location locationDest = null;

    /**
     * Creates the shipping label object from the user input data, the uuid and the PEER_NAME.
     *
     * @param sender            Sender of the package.
     * @param origin            Human readable name of the origin location.
     * @param locationOrigin    Geolocation of the origin location.
     * @param recipient         Recipeint of the package.
     * @param destination       Human readable name of the reception location.
     * @param locationDest      Geolocation of the reception location.
     * @param packageWeight     Weight of the package.
     */
    public ShippingLabel(String sender, String origin, Location locationOrigin, String recipient, String destination, Location locationDest, double packageWeight) {
        this.packageUUID = UUID.randomUUID();
        this.transferorID = PEER_NAME;
        this.sender = sender;
        this.origin = origin;
        this.locationOrigin = locationOrigin;
        this.recipient = recipient;
        this.destination = destination;
        this.locationDest = locationDest;
        this.packageWeight = packageWeight;
    }

    /**
     * String representation of the ShippingLabel object.
     *
     * @return Formatted string representation of object.
     */
    @Override
    public String toString() {
        return String.format(this.packageUUID + this.transferorID + this.sender + this.origin +
                this.locationOrigin + this.recipient + this.destination +
                this.locationDest + this.packageWeight);
    }

    // Getter methods to get the value of the Object field.
    public UUID getPackageUUID() {
        return this.packageUUID;
    }

    public String getSender() {
        return this.sender;
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public double getPackageWeight() {
        return this.packageWeight;
    }

    public String getDestination() {
        return this.destination;
    }

    public Location getLocationOrigin() {
        return this.locationOrigin;
    }

    public Location getLocationDest() {
        return this.locationDest;
    }
}
