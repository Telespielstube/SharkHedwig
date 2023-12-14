package Location;

import static java.lang.Math.sqrt;

/**
 * Location object to determine the current location of a mobile device or geo location.
 */
public class Location {

    private String name = null;
    private double latitude = 0.0;
    private double longitude = 0.0;

    public Location() {}
    /**
     * This constructor is used when creating the ShippingLabel object.
     *
     * @param latitude  To make the location name more precise the latitude coordinate is given in degree
     * @param longitude To make the location name more precise the longitude coordinate is given in degree
     */
    public Location(double latitude, double longitude) {
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
    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }
}

