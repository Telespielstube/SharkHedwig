package Location;

/**
 * Interface for all thing geo locations.
 */
public interface IGeoSpatial {

    /**
     * This is a very simplified method to calculate distance between two geo location points.
     *
     * @param sender       Location object that holds the latitude and longitude coordinates of the origin.
     * @param recipient    Location object that holds the latitude and longitude coordinates of the destination.
     * @return             Distance between two points.
     */
    double pointToPointDistance(Location sender, Location recipient);

    /**
     * Another simplified method mainly for the "OfferReply" recipient to calculate the distance from its current
     * to transferor locatation to the final package destination.
     *
     * @param sender                Current transferor location
     * @param packageDestination    Location of the package to deliver.
     * @return                      Total distance.
     */
    double toDestination(Location sender, Location packageDestination);

    /**
     * Returns the current location.
     *
     * @return    Location object.
     */
    Location getCurrentLocation();

    /**
     * Sets the location for handover.
     *
     * @param pickupLocation    Location object.
     */
    void setPickUpLocation(Location pickupLocation);
}
