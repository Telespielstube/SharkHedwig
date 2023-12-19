package Location;

/**
 * Interface for all thing geo locations.
 */
public interface ILocation {

    /**
     * BEWARE!! This is a very simplified method to calculate distance between two geo location points.
     *
     * @param sender       Location object that holds the latitude and longitude coordinates of the origin.
     * @param recipient    Location object that holds the latitude and longitude coordinates of the destination.
     * @return             Distance between two points.
     */
    public double pointToPointDistance(Location sender, Location recipient);

    /**
     * Another simplified method to calculate the distance transferee to package destination.
     *
     * @param sender
     * @return
     */
    public double toDestination(Location sender);

    /**
     * Another simplified method mainly for the "OfferReply" recipient to calculate the distance from its current
     * to transferor locatation to the final package destination.
     *
     * @param sender                Current transferor location
     * @param packageDestination    Location of the package to deliver.
     * @return                      Total distance.
     */
    public double toDestination(Location sender, Location packageDestination);
}