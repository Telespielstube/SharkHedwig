package HedwigUITest;

import HedwigUI.UserInput;
import HedwigUI.UserManager;
import Setup.SharkHedwigComponent;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.pki.SharkPKIComponent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;


public class UserManagerTest {

    private static final UserInput userInputBuilder = new UserInput("Alice", "HTW-Berlin",
            52.456931, 13.526444, "Bob", "Ostbahnhof", 52.5105, 13.4346, 1.2);
    private SharkPKIComponent sharkPKIComponent;

    @Test
    public void testIfShippingLabelChangesProtocolState() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, SharkException {
        SharkHedwigComponent sharkHedwigComponent = Mockito.mock(SharkHedwigComponent.class);
        sharkHedwigComponent.setupComponent();
        // Not finished yet
    }

}
