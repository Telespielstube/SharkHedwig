package Misc;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.SecureRandom;

import static Misc.Constants.PEER_NAME;

/**
 * The Utilities class is a collection of small small useful methods like
 * CheckDroneID()
 */
public class Utilities {

    /**
     * Sets a global boolean variable if the mobile device has carriage. Initial variable state is false.
     * The variable gets set to 'true' if the shipping label form is filled out correctly.
     */
    public static boolean hasCarriage = false;

    /**
     * !! Attention this is just an example class to show how a simple ID verification works.
     * This is a very simple methode to verify the incoming peerName is conform to the hard coded ID.
     * The regex only matches the Id in the form of:
     * Upper followed by 4 digits ranging from 0-9 followed by 4 characters lower or upper case.
     *
     * @param args   contains the ID of the drone hardware.
     * @return true if id matches the regex.
     */
    public static Boolean checkDroneID() {
        return PEER_NAME.matches("Hedwig123");
    }

    /**
     * Creates a human readable ISO-8601 conform timestamp.
     *
     * @return    timestamp as human readable String object.
     */
    public static String getReadableTimestamp() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
    }

    /**
     * Generates a sceure random number. The SecureRandom class generates a number up to 128 bits.
     * Makes the chance of repeating smaller.
     *
     * @return    String representaiton of secure random number.
     */
    public static String generateRandomNumber() {
        SecureRandom random = new SecureRandom();
        return Integer.toString(random.nextInt());
    }

    /**
     * Returns the current time in milliseconds.
     * @return    time in milliseconds, between the current time and midnight, January 1, 1970.
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }
}
