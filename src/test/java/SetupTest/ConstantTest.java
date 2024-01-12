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
        assertEquals("hedwig" , AppConstant.APP_FORMAT.toString());
    }

    @Test
    public void testIfAssertNotEqualsTestIsPassed() {
        assertNotEquals("hedwig", AppConstant.PEER_FOLDER.toString());
    }

    @Test
    public void testIfAssertNotEqualsTestIsPassedWhenGetterMethodeIsMissing() {
        assertNotEquals("hedwig", AppConstant.CA_ID);
    }
}
