package HedwigUI;

import DeliveryContract.ShippingLabel;

public class UserObserver implements IUserObserver {

    private UserInputBuilder userInputBuilder;
    private ShippingLabel shippingLabel;

    @Override
    public void userObjectCreated(UserInputBuilder userInputBuilder) {
        shippingLabel.create(userInputBuilder);
    }
}
