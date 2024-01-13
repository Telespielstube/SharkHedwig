package Setup;

import net.sharksystem.SharkComponent;
import net.sharksystem.SharkComponentFactory;
import net.sharksystem.pki.SharkPKIComponent;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class SharkHedwigComponentFactory implements SharkComponentFactory {
    private final SharkPKIComponent sharkPKIComponent;
    public SharkHedwigComponentFactory(SharkPKIComponent sharkPKIComponent){
        this.sharkPKIComponent = sharkPKIComponent;
    }

    @Override
    public SharkComponent getComponent() {
        try {
            return new SharkHedwigComponent(sharkPKIComponent);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
