package SetupTest;

import Setup.AppConstant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ConstantTest {

    @Test
    public void testAllUnitTestInThisClass(){
        testIfHedwigStringGetsReturnedFromAppConstantsEnumClass();
        testIfAssertNotEqualsTestIsPassed();
        testIfAssertNotEqualsTestIsPassedWhenGetterMethodeIsMissing();
    }
    @Test
    public void testIfHedwigStringGetsReturnedFromAppConstantsEnumClass() {
        assertEquals("hedwig" , AppConstant.AppFormat.toString());
    }

    @Test
    public void testIfAssertNotEqualsTestIsPassed() {
        assertNotEquals("hedwig", AppConstant.PeerFolder.toString());
    }

    @Test
    public void testIfAssertNotEqualsTestIsPassedWhenGetterMethodeIsMissing() {
        assertNotEquals("hedwig", AppConstant.CaId);
    }
}
