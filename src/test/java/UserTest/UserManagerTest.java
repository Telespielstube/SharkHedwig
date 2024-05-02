package UserTest;

import User.UserInput;
import User.UserManager;
import net.sharksystem.SharkException;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.jupiter.api.Test;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UserManagerTest {

    private static UserManager userManager = new UserManager();
    private final String json = "{ \"sender\" : \"Marta\", \"origin\" : \"HTW-Berlin\", \"latitudeOrigin\" : \"80.0\", " +
        "\"longitudeOrigin\" : \"90.0\" , \"recipient\" : \"Peter\", \"destination\" : \"Ostbahnhof\", " +
        "\"latitudeDest\" : \"44.0\", \"longitudeDest\" : \"67.0\", \"packageWeight\" : \"100\"}";

    private static final UserInput userInputBuilder = new UserInput("Alice", "HTW-Berlin",
            52.456931, 13.526444, "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
    private SharkPKIComponent sharkPKIComponent;

    @Test
    public void testIfJsonDataGetsParsedToShippingLabelObject() {
        userManager.processJson(json);
    }

}
