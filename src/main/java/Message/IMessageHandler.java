package Message;

import Channel.Type;
import net.sharksystem.asap.ASAPException;

import java.io.*;

public interface IMessageHandler {

    Object parseMessage(byte[] message, String senderE2E);
    /**
     * Prepares the passed message object for sending to other devices.
     *
     * @param object    A Generic Message object. It accepts all Message object because we want one 
     *                  build methode not a methode for every message object.
     */
    <T> void buildOutgoingMessage(T object, Type uri, String appName);

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
     * Serializes the passed byte message.
     *
     * @param byteMessage
     * @param receiver
     *
     * @return
     */
//     byte[] serializeMessage(byte[] byteMessage, CharSequence receiver);
//
//    /**
//     * Encrypts and signs the serialized byte message for a secure transmission.The used crypto algorithms are SHA-256 and RSA.
//     *
//     * @param byteMessage    the serialized byte message.
//     * @param receiver       the recipient of the message.
//     *
//     * @return               encrypted byte[] message.
//     */
//    byte[] encryptMessage(byte[] byteMessage, CharSequence receiver);
//
//    /**
//     * Method decryptes the deserialized message.
//     *
//     */
//    boolean decryptMessage(byte[] message, String senderE2E);
//
//
//    /**
//     * Parses the byte message content to a message object.
//     *
//     * @param serializedMessage byte array of the decrypted but still serialized Message.
//     * @param next
//     * @return byte array of the deserialized message.
//     */
//    byte[] deserializeMessage(byte[] next) throws IOException, ASAPException;
}
