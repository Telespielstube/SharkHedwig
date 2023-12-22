package Message;

import net.sharksystem.pki.SharkPKIComponent;

public interface IMessageHandler {

    Object parseMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent);
    /**
     * Prepares the passed message object for sending to other devices.
     *
     * @param object    A Generic Message object. It accepts all Message object because we want one 
     *                  build methode not a methode for every message object.
     */
    <T> byte[] buildOutgoingMessage(T object, String uri, String recipient, SharkPKIComponent sharkPKIComponent);

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
}
