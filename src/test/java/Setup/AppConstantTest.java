package Setup;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AppConstantTest {

    @Test
    public void testAllUnitTestInThisClass(){
        testIfHedwigStringGetsReturnedFromAppConstantsEnumClass();
        testIfAssertNotEqualsTestIsPassed();
        testIfAssertNotEqualsTestIsPassedWhenGetterMethodeIsMissing();
    }
    @Test
    public void testIfHedwigStringGetsReturnedFromAppConstantsEnumClass() {
        assertEquals("hedwig" ,AppConstant.AppFormat.getAppConstant());
    }

    @Test
    public void testIfAssertNotEqualsTestIsPassed() {
        assertNotEquals("hedwig", AppConstant.PeerFolder.getAppConstant());
    }

    @Test
    public void testIfAssertNotEqualsTestIsPassedWhenGetterMethodeIsMissing() {
        assertNotEquals("hedwig", AppConstant.CaId);
    }
}
