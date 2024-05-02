package User;

import Notification.EmailService;

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
    void create(UserInput data);

    /**
     * Sets up the email serivce (eg. Mailtrap offers a email testing environemnt)
     */
    void setupEmailService(EmailService data);
}
