package HedwigUI;

// Methods to be implemented in the UserInterface class.
public interface IUserInterface {

    /**
     * Reads the standard input stream.
     *
     * @return    A String object of the users console input.
     */
    String readUserInput();

    /**
     * Shipping label form that allows the user to input necessary fundamental data for safe package shipping.
     *
     * @param infoText    A short info text for the user.
     */
    void shippingLabelForm(String infoText);

    /**
     * Checks if all input data is correct.
     */
    void checkFormData(String userInfo);

    /**
     * Prints all input data so the user can double-check their input.
     *
     * @return  True if the user confirmed their input data.
     */
    boolean acceptInput();
}
