package HedwigUI;

import DeliveryContract.ShippingLabel;

/** Functional interface for user generated cshipping label.
 *
 */
public interface IUserInterface {

    boolean create(UserInput userInput);
}
