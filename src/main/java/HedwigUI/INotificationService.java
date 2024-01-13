package HedwigUI;

/**
 * Users need to be informed about an arriving package.
 */
public interface INotificationService {

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
