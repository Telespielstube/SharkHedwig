package SetupTest;

import Setup.Constant;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ConstantTest {

    @Test
    public void testAllUnitTestInThisClass(){
        testIfHedwigStringGetsReturnedFromAppConstantsEnumClass();
        testIfAssertNotEqualsTestIsPassed();
        testIfAssertNotEqualsTestIsPassedWhenGetterMethodeIsMissing();
    }
    @Test
    public void testIfHedwigStringGetsReturnedFromAppConstantsEnumClass() {
        Assert.assertEquals("hedwig" , Constant.AppFormat.getAppConstant());
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
