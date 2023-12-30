package LocationTest;

import Location.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {

    private static Location location;
    @BeforeAll
    public static void setup() {
        location = new Location("HTW-Berlin", 52.456931, 13.526444);
    }

    @Test
    public void returnHTWLatitude() {
        assertEquals(52.456931, location.getLatitude(), 0.001);
    }

    @Test
    public void returnHTWLongitude() {
        assertEquals(13.526444, location.getLongitude(), 0.001);
    }
}
