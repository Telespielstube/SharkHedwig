package Message;

import net.sharksystem.pki.SharkPKIComponent;

public interface IMessageHandler {

    /**
     * Parses the received message from a signed and encrypted message back to a verified decrypted message.
     *
     * @param message              Message content
     * @param senderE2E            Message sender
     * @param sharkPKIComponent    Public-key infarastructure componente
     * @return                     verified and decrypted message.
     */
    Object parseMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent);

    /**
     * Prepares the passed message object for sending to other devices.
     *
     * @param object    A Generic Message object. It accepts all Message object because we want one 
     *                  build methode not a methode for every message object.
     */
    byte[] buildOutgoingMessage(Object object, String uri, String recipient, SharkPKIComponent sharkPKIComponent);

   // void sendMessage(APP_FORMAT, String uri.toString(), byte[] signedMessage);
    /**
     * Convert the passed message object to a byte array.
     *
     * @param object    A Generic Message object. It accepts all Message object because we want one 
     *                  build methode not a methode for every message object.
     */
    byte[] objectToByteArray(Object object);

    /**
     * Converts a byte array which reprints the message to the correct Message object.
     *
     * @param message    byte[] message that need to be converted into a Object representation.
     *
     * @return           The object containing the message content.
     */
    Object byteArrayToObject(byte[] message);
}
