package SessionTest;

import Misc.Utilities;
import Session.IdentificationSession.IdentificationSession;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class IdentificationSessionTest {

    IdentificationSession identificationSession = new IdentificationSession();
    @Test
    public void testIfSecureRandomNumberIsReturnedAsString() {
        System.out.println(identificationSession.generateRandomNumber());
        assertNotNull(identificationSession.generateRandomNumber());
    }
}
