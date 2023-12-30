package SetupTest;

import Setup.Constant;
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
        assertEquals("hedwig" , Constant.AppFormat.getAppConstant());
    }

    @Test
    public void testIfAssertNotEqualsTestIsPassed() {
        assertNotEquals("hedwig", Constant.PeerFolder.getAppConstant());
    }

    @Test
    public void testIfAssertNotEqualsTestIsPassedWhenGetterMethodeIsMissing() {
        assertNotEquals("hedwig", Constant.CaId);
    }
}
