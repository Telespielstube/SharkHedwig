package Misc;

import org.junit.Test;

import javax.rmi.CORBA.Util;

import static org.junit.Assert.*;

/**
 * JUnitTest class for all message relevant tests. The method declarations are very self-explanatory.
 * * Just to make it easier to understand what unit is tested.
 */
public class UtilitiesTest {

    @Test
    public void testIfIDReadableTimestampIsReturned() {
        String timestamp = Utilities.createReadableTimestamp();
        System.out.println(timestamp);
        assertNotNull(timestamp);
    }



    @Test
    public void testIfSystemReturnsTimeInMillis() {
        assertEquals(System.currentTimeMillis(), Utilities.createTimestamp(), 0.1);
    }
}