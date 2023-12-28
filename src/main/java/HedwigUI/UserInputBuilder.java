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
    private double latitudeOrigin = 0.0;
    private double longitudeOrigin = 0.0;
    private double latitudeDest = 0.0;
    private double longitudeDest = 0.0;
    private double packageWeight = 0.0;

    public UserInputBuilder() {}

    public UserInputBuilder(String sender, String origin, double latitudeOrigin,
                            double longitudeOrigin, String recipient, String destination,
                            double latitudeDest, double longitudeDest, double packageWeight) {
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

    public double getLatitudeOrigin() {
        return latitudeOrigin;
    }

    public double getLongitudeOrigin() {
        return longitudeOrigin;
    }

    public double getLatitudeDest() {
        return latitudeDest;
    }

    public double getLongitudeDest() {
        return longitudeDest;
    }

    public double getPackageWeight() {
        return packageWeight;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setLongitudeOrigin(double longitudeOrigin) {
        this.longitudeOrigin = longitudeOrigin;
    }

    public void setLatitudeOrigin(double latitudeOrigin) {
        this.latitudeOrigin = latitudeOrigin;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setLongitudeDest(double longitudeDest) {
        this.longitudeOrigin = longitudeDest;
    }

    public void setlatitudeDest(double latitudeDest) {
        this.latitudeDest = latitudeDest;
    }

    public void setPackageWeight(float packageWeight) {
        this.packageWeight = packageWeight;
    }

    public UserInputBuilder create() {
        return new UserInputBuilder(this.sender, this.origin, this.latitudeOrigin, this.longitudeOrigin,
                this.recipient, this.destination, this.latitudeDest, this.longitudeDest, this.packageWeight);
    }
}