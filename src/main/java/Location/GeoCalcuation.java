package Location;

import static Misc.Constants.PARALLEL_OF_LATITUDE;
import static Misc.Constants.PARALLEL_OF_LONGITUDE;
import static java.lang.Math.sqrt;

public class GeoCalcuation {

    public GeoCalcuation() {}

    public double calculateDistance(Location sender, Location recipient) {
        double dx = PARALLEL_OF_LATITUDE * (sender.getLatitude() - recipient.getLatitude()); // delta of latitude points
        double dy = PARALLEL_OF_LONGITUDE * (sender.getLongitude() - recipient.getLongitude()); // delta of longitude points
        return sqrt(dx * dx + dy * dy);
    }
}
