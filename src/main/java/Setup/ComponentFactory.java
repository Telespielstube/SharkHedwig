package Setup;

import net.sharksystem.SharkComponent;
import net.sharksystem.SharkComponentFactory;
import net.sharksystem.pki.SharkPKIComponent;

public class ComponentFactory implements SharkComponentFactory {
    private final SharkPKIComponent sharkPKIComponent;
    public ComponentFactory(SharkPKIComponent sharkPKIComponent){
        this.sharkPKIComponent = sharkPKIComponent;
    }

    @Override
    public SharkComponent getComponent() {
        return new Component(sharkPKIComponent);
    }
}
