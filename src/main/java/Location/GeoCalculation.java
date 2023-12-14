package Location;

import static Misc.Constants.PARALLEL_OF_LATITUDE;
import static Misc.Constants.PARALLEL_OF_LONGITUDE;
import static java.lang.Math.sqrt;

public class GeoCalculation implements ILocation {

    public GeoCalculation() {}

    public double calculateDistance(Location sender, Location recipient) {
        double dx = Paralelle.ParallelOfLatitude.getParalelle() * (sender.getLatitude() - recipient.getLatitude()); // delta of latitude points
        double dy = Paralelle.ParallelOfLongitude.getParalelle() * (sender.getLongitude() - recipient.getLongitude()); // delta of longitude points
        return sqrt(dx * dx + dy * dy);
    }
}
