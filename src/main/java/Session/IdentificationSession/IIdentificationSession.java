package Session.IdentificationSession;

import Message.IMessageHandler;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;

public interface IIdentificationSession {

    void initializeSession();

    <T> void parseMessage(T message);
    /**
     * Generates a sceure random number. The SecureRandom class generates a number up to 128 bits.
     * Makes the chance of repeating smaller.
     *
     * @return    The secure random number converted to byte[] ready for encryption.
     */
    byte[] generateRandomNumber();

    /**
     * Encrypts the secure random number for the challenge-response methode.
     *
     * @return    Encrypted challenge number.
     */
    byte[] encryptRandomNumber();

    /**
     * Returns the current time in milliseconds.
     *
     * @return    time in milliseconds, between the current time and midnight, January 1, 1970.
     */
    long createTimestamp();

}
