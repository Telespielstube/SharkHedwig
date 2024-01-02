package HedwigUI;

import DeliveryContract.ShippingLabel;

public class UserObserver implements IUserObserver {

    private UserInputBuilder userInputBuilder;

    @Override
    public void userObjectCreated(UserInputBuilder userInputBuilder) {
        ShippingLabel shippingLabel = null;
        shippingLabel.create(userInputBuilder);
    }
}
