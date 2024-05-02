package User;

import DeliveryContract.ShippingLabel;
import Location.Location;
import Misc.Utilities;
import Notification.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * The UserManager class is responsible for managing everything the user is able to interact with the SharkHedwigComponent.
 */
public class UserManager implements Manageable {
    private ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    /**
     * Validates the received JSON data for further processing.
     * @param jsonData
     * @return
     */
    private boolean isValid(String jsonData) {
        try {
            mapper.readTree(jsonData);
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;
    }

    @Override
    public void processJson(String jsonData) {
        try {
            if (isValid(jsonData)) {
                UserInput data = mapper.readValue(jsonData, UserInput.class);
                create(data);
            }
        } catch (JsonProcessingException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an Exception while trying to parse JSON file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(UserInput data) {
        if (validate(data)) {
            new ShippingLabel.Builder(
                     Utilities.createUUID(),
                     data.getSender(),
                     data.getOrigin(),
                     new Location(data.getLatitudeOrigin(), data.getLongitudeOrigin()),
                     data.getRecipient(),
                     data.getDestination(),
                     new Location(data.getLatitudeDest(), data.getLongitudeDest()),
                     data.getPackageWeight()).build();
        }
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
    public void setupEmailService(EmailService data) {

    }
}
