package User;

import DeliveryContract.ShippingLabel;
import Location.Location;
import Misc.Utilities;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The UserManager class is responsible for managing everything the user is able to interact with the SharkHedwigComponent.
 */
public class UserManager implements Manageable {

    @Override
    public void create(UserInput data) {
        Optional<ShippingLabel> shippingLabel = Optional.empty();
        shippingLabel = validate(data) ? Optional.of(new ShippingLabel.Builder(
                     Utilities.createUUID(),
                     data.getSender(),
                     data.getOrigin(),
                     new Location(data.getLatitudeOrigin(), data.getLongitudeOrigin()),
                     data.getRecipient(),
                     data.getDestination(),
                     new Location(data.getLatitudeDest(), data.getLongitudeDest()),
                     data.getPackageWeight()).build())
        : Optional.empty();
        shippingLabel.ifPresent(v -> v.setIsCreated(true));
    }

    /**
     * Verifies the user input if no attribute has a null value. If only one field is null no shipping
     * label is created.
     */
    private boolean validate(UserInput data) {
        return Stream.of(
                data.getSender(),
                data.getOrigin(),
                data.getLatitudeOrigin(),
                data.getLongitudeOrigin(),
                data.getRecipient(),
                data.getDestination(),
                data.getLatitudeDest(),
                data.getLongitudeDest(),
                data.getPackageWeight()).anyMatch(Objects::nonNull);
    }

    @Override
    public void setupEmailService() {
    }

    @Override
    public void loginToService() {
    }

    @Override
    public void newMessage() {
    }

    @Override
    public void sendMessage() {
    }
}
