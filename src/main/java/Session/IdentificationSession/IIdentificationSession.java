package Session.IdentificationSession;

public interface IIdentificationSession {

    void initializeSession(String sender);

    /**
     * Generates a sceure random number. The SecureRandom class generates a number up to 128 bits.
     * Makes the chance of repeating smaller.
     *
     * @return    String representaiton of secure random number.
     */
    int generateRandomNumber();

    /**
     * Returns the current time in milliseconds.
     * @return    time in milliseconds, between the current time and midnight, January 1, 1970.
     */
    long getTimestamp();
}
