package Misc;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Utilities class is a collection of small small useful methods like
 * CheckDroneID()
 */
public class Utilities {

    /**
     * Creates a human readable ISO-8601 conform timestamp.
     *
     * @return    timestamp as human readable String object.
     */
    public static String createReadableTimestamp() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
    }

    /**
     * Returns the current time in milliseconds.
     *
     * @return    time in milliseconds, between the current time and midnight, January 1, 1970.
     */
    public static long createTimestamp() {
        return System.currentTimeMillis();
    }
}
