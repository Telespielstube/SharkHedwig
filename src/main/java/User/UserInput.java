package User;

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

    public UserInput(String sender, String origin, Double latitudeOrigin, Double longitudeOrigin, String recipient,
                     String destination, Double latitudeDest, Double longitudeDest, Double packageWeight) {
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

    @JsonGetter("sender")
    public String getSender() {
        return sender;
    }

    @JsonProperty("origin")
    public String getOrigin() {
        return origin;
    }

    @JsonProperty("recipient")
    public String getRecipient() {
        return recipient;
    }

    @JsonProperty("destination")
    public String getDestination() {
        return destination;
    }

    @JsonProperty("latitudeOrigin")
    public Double getLatitudeOrigin() {
        return latitudeOrigin;
    }

    @JsonProperty("longitudeOrigin")
    public Double getLongitudeOrigin() {
        return longitudeOrigin;
    }

    @JsonProperty("LatitudeDest")
    public Double getLatitudeDest() {
        return latitudeDest;
    }

    @JsonProperty("longitudeDest")
    public Double getLongitudeDest() {
        return longitudeDest;
    }

    @JsonProperty("packageWeight")
    public Double getPackageWeight() {
        return packageWeight;
    }
}
