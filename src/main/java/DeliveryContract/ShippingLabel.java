package DeliveryContract;

import Location.Location;
import java.util.Objects;
import java.util.Observable;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * The shipping label object is a core function of the whole protocoll. The label is created after the user confirmed
 * their entry. The input is immutable that means it cannot be changed after confimration.
 * That is why there are no set methods in this class.
 */
public class ShippingLabel extends Observable implements Contractable {

    // Every field has to be an object because the validate methode checks for not null objects.
    private final UUID uuid;
    private final String sender;
    private final String origin;
    private final String recipient;
    private final Double packageWeight;
    private final String destination;
    private final Location locationOrigin;
    private final Location locationDestination;
    private boolean isCreated;

    public ShippingLabel(UUID uuid, String sender, String origin, Location locationOrigin, String recipient,
                         String destination, Location locationDestination, Double packageWeight) {
        this.uuid = uuid;
        this.sender = sender;
        this.origin = origin;
        this.recipient = recipient;
        this.packageWeight = packageWeight;
        this.destination = destination;
        this.locationOrigin = locationOrigin;
        this.locationDestination = locationDestination;
        setIsCreated(true);
        setChanged();
        notifyObservers(this);
    }

    @Override
    public ShippingLabel get() {
        return this;
    }

    @Override
    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    @Override
    public boolean isCreated() {
        return this.isCreated;
    }

    /**
     * After the hand-over is complete the former transferor has to reset all DeliveryContract states to the initial states.
     */
    public void resetContractState() {
        clearChanged();
    }

    /** Getter methods to get the value of the Object field. No setters because the attriibute values where set
     * remote in the user interface.
     */
    public UUID getUUID() {
        return this.uuid;
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
        return String.format("PackageUUID: " + this.uuid + ";\nSender: " + this.sender + ";\nOrigin: " + this.origin +
                ";\nOrigin coordinates: " + this.locationOrigin + ";\nRecipient: " + this.recipient + ";\nDestination: " +
                this.destination + ";\nDestination coordinates: " + this.locationDestination + ";\nPackage weight: " + this.packageWeight + ";\n");
    }

    /**
     * Validates the received shipping label values.
     *
     * @param content    ShippingLabel object.
     *
     * @return           True if all fields have a value other than null. False if at least one field has a null value.
     */
    public static boolean validate(ShippingLabel content) {
        return Stream.of(
                content.getUUID(),
                content.getSender(),
                content.getOrigin(),
                content.getPackageOrigin(),
                content.getRecipient(),
                content.getDestination(),
                content.getPackageDestination(),
                content.getPackageWeight()).anyMatch(Objects::nonNull);
    }
}
