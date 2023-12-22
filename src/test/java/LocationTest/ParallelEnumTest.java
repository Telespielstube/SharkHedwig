package LocationTest;

import Location.Parallel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParallelEnumTest {

    @Test
    public void testAllUnitTestInThisClass(){
        System.out.println("Constant for Latitude: " + Parallel.ParallelOfLatitude);
        assertEquals(111.3 , Parallel.ParallelOfLatitude.getParalelle(), 0.001);

    }
    @Test
    public void testIfConstantForLongitudeIsReturned() {
        System.out.println("Constant for Longitude: " + Parallel.ParallelOfLongitude);
        assertEquals(71.5 , Parallel.ParallelOfLongitude.getParalelle(), 0.001);
    }
}
