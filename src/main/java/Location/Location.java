package Location;

import java.io.Serializable;

import static java.lang.Math.sqrt;

/**
 * Location object to determine the current location of a mobile device or geo location.
 */
public class Location implements Serializable {

    private String name = null;
    private Double latitude = 0.0;
    private Double longitude = 0.0;

    public Location() {}
    /**
     * This constructor is used when creating the ShippingLabel object.
     *
     * @param latitude  To make the location name more precise the latitude coordinate is given in degree
     * @param longitude To make the location name more precise the longitude coordinate is given in degree
     */
    public Location( Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Creates a point with the name and the latitude and longitude coordinates.
     *
     * @param name      human readable point of a location.
     * @param latitude  To make the location name more precise the latitude coordinate is given in degree
     * @param longitude To make the location name more precise the longitude coordinate is given in degree
     */
    public Location(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return ("Latitude: " + this.latitude + "; Longitude: " + this.longitude);
    }
}

