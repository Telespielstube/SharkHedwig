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
     */
    public double pointToPointDistance(Location sender, Location recipient);
}