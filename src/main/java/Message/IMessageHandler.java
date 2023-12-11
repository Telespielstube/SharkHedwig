package Message;

import Channel.Type;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPCryptoParameterStorage;
import net.sharksystem.asap.utils.ASAPSerialization;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Set;

public interface IMessageHandler {

    /**
     * Handles the operation needed to work with the incoming messages.
     * @param message
     */
     void handleIncomingMessage(byte[] message);

    /**
     * Prepares the passed message object for sending to other devices.
     *
     * @param object    A Generic Message object. It accepts all Message object because we want one 
     *                  build methode not a methode for every message object.
     */
    public <T> void buildOutgoingMessage(T object, Type uri, String appName);

    /**
     * Convert the passed message object to a byte array.
     *
     * @param object    A Generic Message object. It accepts all Message object because we want one 
     *                  build methode not a methode for every message object.
     */
    <T> byte[] objectToByteArray(T object);

    /**
     * Converts a byte array which reprints the message to the correct Message object.
     *
     * @param message    byte[] message that need to be converted into a Object representation.
     *
     * @return           The object containing the message content.
     */
    Object byteArrayToObject(byte[] message);

    /**
     * Signs a plain byte[] message. The used vrypto algorithms are SHA-256 and RSA.
     *
     * @param unsignedMessage    the plain serialized byte[] message.
     *
     * @return                   signed byte[] message.
     */
    public byte[] signMessage(byte[] unsignedMessage);

    /**
     * Verifies the signature of a signed message. Just because all protocol messages are signed every message needs
     * to go through this methode.
     *
     * @param signedMessage             byte array representation of the message.
     * @param signatureToBeVerified     is the signature
     *
     * @return                          bool value if message is verified or failed.
     */
     boolean verifySignature(byte[] signedMessage, byte[] signatureToBeVerified);

    /**
     * Serializes the passed byte message.
     *
     * @param byteMessage
     * @param receiver
     *
     * @return
     */
     byte[] serializeMessage(byte[] byteMessage, CharSequence receiver);

    /**
     * Encrypts the serialized byte message for a secure transmission.
     *
     * @param byteMessage    the serialized byte message.
     * @param receiver       the recipient of the message.
     *
     * @return               encrypted byte[] message.
     */
    byte[] encryptMessage(byte[] byteMessage, CharSequence receiver);

    /**
     * First operation the received message needs to go through. Decrypts the received message
     *
     * @param message    byte[] encrypted message.
     *
     * @return           decrypted byte array of message.
     */
    byte[] decryptMessage(byte[] message);

    /**
     * Parses the byte message content to a message object.
     *
     * @param serializedMessage    byte array of the decrypted but still serialized Message.
     *
     * @return                     byte array of the deserialized message.
     */
    byte[] deserializeMessage(byte[] serializedMessage) throws IOException, ASAPException;
}
