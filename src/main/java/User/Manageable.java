package User;

import Notification.EmailService;

/** Functional interface for user generated shipping label.
 *
 */
public interface Manageable {

    /**
     * Parses the received json file to the ShippingLabel object.
     *
     * @param jsonData    String object of the received json file.
     */
    void getJson(String jsonData);
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
