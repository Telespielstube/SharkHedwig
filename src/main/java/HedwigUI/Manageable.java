package HedwigUI;

import DeliveryContract.ShippingLabel;

import java.util.Optional;

/** Functional interface for user generated shipping label.
 *
 */
public interface Manageable {

    /**
     * Creates the ShippingLabel object.
     *
     * @param userInput    Received user input from a different device.
     *
     * @return
     */
    void create(UserInput userInput);

    /**
     * Sets up the email serivce (eg. Mailtrap offers a email testing environemnt)
     */
    void setupEmailService();

    /**
     * Logs in to the service with the provided credentials.
     */
    void loginToService();

    /**
     * Creates a new email notification for the recipient.
     */
    void newMessage();

    /**
     * Sends the new message to the package recipient at the given shipping label destination.
     */
    void sendMessage();


}
