package HedwigUI;

/**
 * This does nothing more than filling up the object constructor with the input data. This can be replaced by a fx JSON
 * library to receive the user input from a phone, tablet, computer....
 */
public class UserInputBuilder {

    private String sender = null;
    private String origin = null;
    private String recipient = null;
    private String destination = null;
    private Double latitudeOrigin = null;
    private Double longitudeOrigin = null;
    private Double latitudeDest = null;
    private Double longitudeDest = null;
    private Double packageWeight = null;

    public UserInputBuilder(String sender, String origin, Double latitudeOrigin, Double longitudeOrigin, String recipient,
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

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setLongitudeOrigin(Double longitudeOrigin) {
        this.longitudeOrigin = longitudeOrigin;
    }

    public void setLatitudeOrigin(Double latitudeOrigin) {
        this.latitudeOrigin = latitudeOrigin;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public void setLongitudeDest(Double longitudeDest) {
        this.longitudeOrigin = longitudeDest;
    }

    public void setlatitudeDest(Double latitudeDest) {
        this.latitudeDest = latitudeDest;
    }

    public void setPackageWeight(Double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public UserInputBuilder create() {
        return new UserInputBuilder(this.sender, this.origin, this.latitudeOrigin, this.longitudeOrigin,
                this.recipient, this.destination, this.latitudeDest, this.longitudeDest, this.packageWeight);
    }
}