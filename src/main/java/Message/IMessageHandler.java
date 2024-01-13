package Message;

import net.sharksystem.pki.SharkPKIComponent;

import java.io.IOException;

public interface IMessageHandler {

    /**
     * Checks if the current message in list is for this device.
     *
     * @param message    message content.
     *
     * @return           true if message is meant for device, false if not.
     */
    boolean checkRecipient(byte[] message);

    /**
     * Parses the received message from a signed and encrypted message back to a verified decrypted message.
     *
     * @param message              Message content
     * @param senderE2E            Message sender
     * @param sharkPKIComponent    Public-key infarastructure componente
     * @return                     verified and decrypted message.
     */
    Object parseIncomingMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent) throws IOException;

    /**
     * Prepares the passed message object for sending to other devices.
     *
     * @param object    A Generic Message object. It accepts all Message object because we want one
     *                  build methode not a methode for every message object.
     */
    byte[] buildOutgoingMessage(Object object, String uri, String recipient, SharkPKIComponent sharkPKIComponent);
}
