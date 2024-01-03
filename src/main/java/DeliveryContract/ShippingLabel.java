package DeliveryContract;

import HedwigUI.UserInputBuilder;
import Location.Location;
import Misc.Utilities;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * The shipping label object is a core function of the whole protocoll. The label is created after the user confirmed
 * their entry. The input is immutable that means it cannot be changed after confimration.
 * That is why there are no set methods in this class.
 */
public class ShippingLabel implements IDeliveryContract {

    private UUID packageUUID;
    private String sender = null;
    private String origin = null;
    private String recipient = null;
    private Double packageWeight = null;
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
    }

    public ShippingLabel() {}

    /**
     * Creates a new ShippingLabel object passed from the UserInputBuilder object.
     *
     * @param object    UserInputBuilder object sent from the user interface.
     */

    public boolean create(UserInputBuilder object) {
        if (verifyUserData(object)) {
            new ShippingLabel(Utilities.createUUID(), object.getSender(), object.getOrigin(), new Location(object.getLatitudeOrigin(),
                    object.getLongitudeOrigin()), object.getRecipient(), object.getDestination(), new Location(object.getLatitudeDest(),
                    object.getLongitudeDest()), object.getPackageWeight());
            this.isCreated = true;
            return true;
        }
        this.isCreated = false;
        return false;
    }

    /**
     * Verifies the user input if no attribute has a null value. If only one field is null no schipping
     * label is created.
     *
     * @param userInput    UserInputBuilder oject this one needs to be checked before the label gets created.
     */
    public boolean verifyUserData(UserInputBuilder userInput) {
        return Stream.of(userInput.getSender(), userInput.getOrigin(), userInput.getLatitudeOrigin(), userInput.getLongitudeOrigin(),
                userInput.getRecipient(), userInput.getDestination(), userInput.getLatitudeDest(), userInput.getLongitudeDest(),
                userInput.getPackageWeight())
                .anyMatch(Objects::nonNull);
    }

    @Override
    public Object get() {
        return this;
    }

    @Override
    public boolean getIsCreated() {
        return this.isCreated;
    }

    @Override
    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
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

    public Double getPackageWeight() {
        return this.packageWeight;
    }

    /**
     * String representation of the ShippingLabel object.
     *
     * @return Formatted string representation of object.
     */
    @Override
    public String toString() {
        return String.format(this.packageUUID + this.sender + this.origin + this.locationOrigin + this.recipient +
                this.destination + this.locationDest + this.packageWeight);
    }
}
