package HedwigUI;

/**
 * This is just filling up the object constructor with the input data. This object mainly serves for testing purposes.
 * This can be/should be replaced by an e.g. JSON library to receive the user input from a phone, tablet, computer....
 */
public class UserInput {

    private String sender = null;
    private String origin = null;
    private String recipient = null;
    private String destination = null;
    private Double latitudeOrigin = null;
    private Double longitudeOrigin = null;
    private Double latitudeDest = null;
    private Double longitudeDest = null;
    private Double packageWeight = null;

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
