package Location;

import static java.lang.Math.sqrt;

public class GeoSpatial implements Locationable {

    private Location pickUp;

    public GeoSpatial() {
        this.pickUp = null;
    }

    /**
     * For now this is the hard coded location for the HTW-Berlin Building C.
     *
     * @return    Current location of Hedwig drone.
     */
    public Location getCurrentLocation() {
        return new Location("HTW-Berlin", 52.456931, 13.526444);
    }

    public void setPickUpLocation(Location pickupLocation) {
        this.pickUp = pickupLocation;
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
        double dx = Parallel.ParallelOfLatitude.getParallel() *
                (sender.getLatitude() - recipient.getLatitude());
        double dy = Parallel.ParallelOfLongitude.getParallel() *
                (sender.getLongitude() - recipient.getLongitude());
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
