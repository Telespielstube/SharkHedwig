package SetupTest;

import Misc.AppConstant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ConstantTest {

    @Test
    public void testIfHedwigStringGetsReturnedFromAppConstantsEnumClass() {
        assertEquals("sharkHedwig" , AppConstant.APP_FORMAT.toString());
    }

    @Test
    public void checkIfHedwigStorageIsReturned() {
        assertEquals("hedwigStorage", AppConstant.PEER_FOLDER.toString());
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
