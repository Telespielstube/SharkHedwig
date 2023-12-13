package Message;

import Channel.Channel;
import net.sharksystem.pki.SharkPKIComponent;

public interface IMessageHandler {

    <T> T parseMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent);
    /**
     * Prepares the passed message object for sending to other devices.
     *
     * @param object    A Generic Message object. It accepts all Message object because we want one 
     *                  build methode not a methode for every message object.
     */
    <T> byte[] buildOutgoingMessage(T object, Channel uri, String appName, SharkPKIComponent sharkPKIComponent);

   // void sendMessage(APP_FORMAT, String uri.toString(), byte[] signedMessage);
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
     * Compares the timestamp of two Message object. To check if the message response is older than 5 seconds.
     *
     * @param object1    Message that was just received.
     * @param object2    Message in the storage.
     * @param <T>        Generic to be able to compare alll kinds of messages.
     */
    <T> boolean compareTimestamp(T object1, T object2);
}
