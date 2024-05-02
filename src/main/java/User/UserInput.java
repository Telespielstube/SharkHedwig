package User;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is just filling up the object constructor with the input data. This object mainly serves for testing purposes.
 * This can be/should be replaced by an e.g. JSON library to receive the user input from a phone, tablet, computer....
 */

public class UserInput {

    private final String sender;
    private final String origin;
    private final String recipient;
    private final String destination;
    private final Double latitudeOrigin;
    private final Double longitudeOrigin;
    private final Double latitudeDest;
    private final Double longitudeDest;
    private final Double packageWeight;

    @JsonCreator
    public UserInput(@JsonProperty("sender") String sender,
                     @JsonProperty("origin") String origin,
                     @JsonProperty("latitudeOrigin") Double latitudeOrigin,
                     @JsonProperty("longitudeOrigin") Double longitudeOrigin,
                     @JsonProperty("recipient") String recipient,
                     @JsonProperty("destination") String destination,
                     @JsonProperty("latitudeDest") Double latitudeDest,
                     @JsonProperty("longitudeDest") Double longitudeDest,
                     @JsonProperty("packageWeight") Double packageWeight) {
        this.sender = sender;
        this.origin = origin;
        this.latitudeOrigin = latitudeOrigin;
        this.longitudeOrigin = longitudeOrigin;
        this.recipient = recipient;
        this.destination = destination;
        this.latitudeDest = latitudeDest;
        this.longitudeDest = longitudeDest;
        this.packageWeight = packageWeight;
    }

    public String getSender() {
        return sender;
    }

    public String getOrigin() {
        return origin;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getDestination() {
        return destination;
    }

    public Double getLatitudeOrigin() {
        return latitudeOrigin;
    }

    public Double getLongitudeOrigin() {
        return longitudeOrigin;
    }

    public Double getLatitudeDest() {
        return latitudeDest;
    }

    public Double getLongitudeDest() {
        return longitudeDest;
    }

    public Double getPackageWeight() {
        return packageWeight;
    }
}
