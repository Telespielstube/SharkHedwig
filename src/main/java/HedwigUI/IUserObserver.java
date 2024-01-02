package HedwigUI;

/**
 * Observer is called when a new UserInputBuilder object is created.
 */
public interface IUserObserver {

    /**
     * Called when user input confirmation created a new UserInputBuilder object.
     */
    void userObjectCreated(UserInputBuilder userInputBuilder);
}
