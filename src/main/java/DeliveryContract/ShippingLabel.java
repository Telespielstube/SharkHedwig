package DeliveryContract;

import Location.Location;
import java.util.Observable;
import java.util.UUID;

/**
 * The shipping label object is a core function of the whole protocoll. The label is created after the user confirmed
 * their entry. The input is immutable that means it cannot be changed after confimration.
 * That is why there are no set methods in this class.
 */
public class ShippingLabel extends Observable implements Contractable {

    // Every field has to be an object because the validate methode checks for not null objects.
    private boolean isCreated;
    private final UUID packageUUID;
    private final String sender;
    private final String origin;
    private final String recipient;
    private final Double packageWeight;
    private final String destination;
    private final Location locationOrigin;
    private final Location locationDestination;

    private ShippingLabel(Builder builder) {
        this.packageUUID = builder.packageUUID;
        this.sender = builder.sender;
        this.origin = builder.origin;
        this.locationOrigin = builder.locationOrigin;
        this.recipient = builder.recipient;
        this.destination = builder.destination;
        this.locationDestination = builder.locationDestination;
        this.packageWeight = builder.packageWeight;
        this.isCreated = true;
        setChanged();
        notifyObservers(this);
    }

    @Override
    public ShippingLabel get() {
        return this;
    }

    @Override
    public boolean getIsCreated() {
        return this.isCreated;
    }

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
        return this.locationDestination;
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
        return String.format("PackageUUID: " + this.packageUUID + ";\nSender: " + this.sender + ";\nOrigin: " + this.origin +
                ";\nOrigin coordinates: " + this.locationOrigin + ";\nRecipient: " + this.recipient + ";\nDestination: " +
                this.destination + ";\nDestination coordinates: " + this.locationDestination + ";\nPackage weight: " + this.packageWeight + ";\n");
    }

    /**
     * This inner class is helful in not exposing the sensetive shippingLabel creation to the public. The creation
     * proccess is delegated to the Builder class. Which is accessible through the public interface to other components.
     */
    public static class Builder {

        private final UUID packageUUID;
        private final String sender;
        private final String origin;
        private final Location locationOrigin;
        private final String recipient;
        private final String destination;
        private final Location locationDestination;
        private final Double packageWeight;

        /**
         * Creates the shipping label object from the user input data, the uuid and the PEER_NAME.
         *
         * @param sender            Sender of the package.
         * @param origin            Human readable name of the origin location.
         * @param locationOrigin    Geolocation of the origin location.
         * @param recipient         Recipient of the package.
         * @param destination       Human readable name of the reception location.
         * @param locationDestination      Geolocation of the reception location.
         * @param packageWeight     Weight of the package.
         */
        public Builder(UUID packageUUID, String sender, String origin, Location locationOrigin, String recipient, String destination, Location locationDestination, Double packageWeight) {
            this.packageUUID = packageUUID;
            this.sender = sender;
            this.origin = origin;
            this.recipient = recipient;
            this.packageWeight = packageWeight;
            this.destination = destination;
            this.locationOrigin = locationOrigin;
            this.locationDestination = locationDestination;

        }

        /**
         * Creates the ShippingLabel object by calling the private constructor.
         *
         * @return    new ShippingLabel object.
         */
        public ShippingLabel build() {
            return new ShippingLabel(this);
        }
    }
}
