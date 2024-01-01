package DeliveryContract;

import HedwigUI.UserInputBuilder;
import Location.Location;
import Misc.Utilities;

import java.util.UUID;

/**
 * The shipping label object is a core function of the whole protocoll. The label is created after the user confirmed
 * their entry. The input is immutable that means it cannot be changed after confimration.
 * That is why there are no set methods in this class.
 */
public class ShippingLabel implements IContractComponent {

    private UUID packageUUID;
    private final String transferorID = null;
    private String sender = null;
    private String origin = null;
    private String recipient = null;
    private double packageWeight = 0.0;
    private String destination = null;
    private Location locationOrigin = null;
    private Location locationDest = null;
    private boolean isCreated = false;

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
    public ShippingLabel(UUID packageUUID, String sender, String origin, Location locationOrigin, String recipient, String destination, Location locationDest, double packageWeight) {
        this.packageUUID = packageUUID;
        this.sender = sender;
        this.origin = origin;
        this.locationOrigin = locationOrigin;
        this.recipient = recipient;
        this.destination = destination;
        this.locationDest = locationDest;
        this.packageWeight = packageWeight;
        this.isCreated = true;
    }

    public ShippingLabel() {}

    /**
     * Creates a new ShippingLabel object passed from the UserInputBuilder object.
     * @param object
     */
    @Override
    public ShippingLabel create(Object object) {
        UserInputBuilder userInput = (UserInputBuilder) object;
        return new ShippingLabel(Utilities.createUUID(), userInput.getSender(), userInput.getOrigin(),
                new Location(userInput.getLatitudeOrigin(), userInput.getLongitudeOrigin()),
                userInput.getRecipient(), userInput.getDestination(), new Location(userInput.getLatitudeDest(),
                userInput.getLongitudeDest()), userInput.getPackageWeight());

    }

    @Override
    public Object get() {
        return this;
    }

    @Override
    public boolean isCreated() {
        return this.isCreated;
    }

    // Getter methods to get the value of the Object field. No setters because the attriibute values where set
    // remote in the user interface.
    public UUID getUUID() {
        return this.packageUUID;
    }

    public String getSender() {
        return this.sender;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Location getPackageOrigin() {
        return this.locationOrigin;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getDestination() {
        return this.destination;
    }


    public Location getPackageDestination() {
        return this.locationDest;
    }

    public double getPackageWeight() {
        return this.packageWeight;
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
}
