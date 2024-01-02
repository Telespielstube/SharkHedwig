package Misc;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * The Utilities class is a collection of small small useful methods like
 * CheckDroneID()
 */
public class Utilities {

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

    /**
     * Encrypts a byte[] data type to create a digital signed document.
     *
     * @param unencrypted    unencrypted document.
     * @param publicKey      Public key to encrypt.
     *
     * @return    Encrypted challenge number.
     */
    public static byte[] encryptAsymmetric(byte[] unencrypted, Key publicKey) {
        Cipher cipher = null;
        byte[] encrypted = new byte[0];
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encrypted = cipher.doFinal(unencrypted);
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException |IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return encrypted;
    }
}
