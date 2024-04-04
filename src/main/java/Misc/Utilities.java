package Misc;

import javax.rmi.CORBA.Util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * The Utilities class is a collection of small small useful methods like
 * CheckDroneID()
 */
public class Utilities {

    private Utilities() {}
    /**
     * Creates a Version 4 (randomly generated) UUID which is an identifier that is
     * unique across both space and time
     *
     * @return    A 128-bit randomly created UUID.
     */
    public static UUID createUUID() {
        return UUID.randomUUID();
    }

    /**
     * Creates a human readable ISO-8601 conform timestamp.
     *
     * @return    timestamp as human readable String object.
     */
    public static String formattedTimestamp() {
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
