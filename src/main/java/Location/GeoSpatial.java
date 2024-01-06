package Location;

import static java.lang.Math.sqrt;

public class GeoSpatial implements IGeoSpatial {

    private Location pickUp;

    public GeoSpatial() {}

    /**
     * For now this is the hard coded location for the HTW-Berlin Building C.
     *
     * @return    Current location of Hedwig drone.
     */
    public Location getCurrentLocation() {
        return new Location();
    }

    public void setPickUpLocation(Location pickUp) {
        this.pickUp = pickUp;
    }

    /**
     * Calculates the distance in kilometers from one point to another point.
     *
     * @param sender       Location object that holds the latitude and longitude coordinates of the origin.
     * @param recipient    Location object that holds the latitude and longitude coordinates of the destination.
     *
     * @return             Distance in kilometers.
     */
    @Override
    public double pointToPointDistance(Location sender, Location recipient) {
        double dx = Parallel.ParallelOfLatitude.getParalelle() * (sender.getLatitude() - recipient.getLatitude()); // delta of latitude points
        double dy = Parallel.ParallelOfLongitude.getParalelle() * (sender.getLongitude() - recipient.getLongitude()); // delta of longitude points
        return sqrt(dx * dx + dy * dy);
    }

    /**
     *
     * @param sender                Current transferor location
     * @param packageDestination    Location of the package to deliver.
     * @return
     */
    @Override
    public double toDestination(Location sender, Location packageDestination) {
        return 0;
    }
}
