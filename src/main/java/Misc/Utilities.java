package Misc;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
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

    /**
     * Encrypts a byte[] data type to create a digital signed document.
     *
     * @param unencrypted    unencrypted document.
     * @param publicKey      Public key to encrypt.
     *
     * @return    Encrypted challenge number.
     */
    public static byte[] encryptRandomNumber(byte[] unencrypted, Key publickey) {
        Cipher cipher = null;
        byte[] encrypted = new byte[0];
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publickey);
            encrypted = cipher.doFinal(unencrypted);
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException |IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    /**
     * Decrypts an encrypted byte[] primitive data type[].
     *
     * @param encrypted     Encrypted document.
     * @param privateKey    Private key to decrypt the document.
     *
     * @return              Decrypted document.
     */
    public static byte[] decryptRandomNumber(byte[] encrypted, Key privateKey) {
        Cipher cipher;
        byte[] decrypted = new byte[0];
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decrypted = cipher.doFinal(encrypted);
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchPaddingException |IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return decrypted;
    }

    /**
     * Digitally signs the TransitRecord object.
     *
     * @param unsigned    Serialized unencrypted TransitRecord object.
     * @param privateKey     Private key for digitally signing.
     *
     * @return               signed object as byte[].
     */
    public static byte[] digitalSign(byte[] unsigned, PrivateKey privateKey) {
        byte[] signed = new byte[1024];
        try {
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initSign(privateKey);
            signature.update(unsigned);
            signed = signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
        return signed;
    }
}