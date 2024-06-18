package Message;

import Misc.Utilities;
import Misc.AppConstant;
import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms.EncryptedMessagePackage;
import net.sharksystem.asap.utils.ASAPSerialization;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.*;

public class MessageHandler implements Handleable {

    private EncryptedMessagePackage encryptedMessagePackage;

    public MessageHandler() {}

    public boolean checkRecipient(byte[] message) {
        try {
            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            if (!this.encryptedMessagePackage.getReceiver().equals(AppConstant.PEER_NAME.toString())) {
                return false;
            }
        } catch (IOException | ASAPException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an ASAP or IOException while checking the recipient of the message: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return true;
    }

    public Object parseIncomingMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        try {
            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            byte[] decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(this.encryptedMessagePackage, sharkPKIComponent.getASAPKeyStore());
            return byteArrayToObject(verifySignedMessage(decryptedMessage, senderE2E, sharkPKIComponent));
        } catch (ASAPException | IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an ASAPException while parsing the incoming message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies the decrypted message by splitting the message part in 2 byte[]. Because verification needs the
     * signed message part and the unisgned message part to verify if the identity of the sender is corrent.
     *
     * @return    the verified byte[] message .
     */
    private byte[] verifySignedMessage(byte[] decryptedSignedMessage, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptedSignedMessage);
        try {
            byte[] byteMessage = ASAPSerialization.readByteArray(inputStream);
            byte[] signedMessage = ASAPSerialization.readByteArray(inputStream);
            if (ASAPCryptoAlgorithms.verify(signedMessage, byteMessage, senderE2E, sharkPKIComponent.getASAPKeyStore())) {
                return byteMessage;
            }
        } catch (ASAPException | IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an ASAPException while verifying the incoming message: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return new byte[0];
    }

    public byte[] buildOutgoingMessage(Object object, String uri, String recipient,
                                       SharkPKIComponent sharkPKIComponent) {
        byte[] byteMessage = objectToByteArray(object);
        try {
            byte[] signedMessage = composeSignedMessage(byteMessage, sharkPKIComponent);
            return ASAPCryptoAlgorithms.produceEncryptedMessagePackage(signedMessage, recipient,
                    sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an ASAPException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Composes a signature message package from the passed byte message and the byte message. The package consists of the
     * signed message and the unsigned message.
     *
     * @param byteMessage    The message  object as byte[].
     * @param sharkPKIComponent         PKIComponent to be able to sign the byte[] message.
     * @return                          byte[] containing the signed and unsigned message.
     */
    private byte[] composeSignedMessage(byte[] byteMessage, SharkPKIComponent sharkPKIComponent) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] signedMessagePackage = ASAPCryptoAlgorithms.sign(byteMessage, sharkPKIComponent.getASAPKeyStore());
            ASAPSerialization.writeByteArray(signedMessagePackage, outputStream);
            ASAPSerialization.writeByteArray(byteMessage, outputStream);
        } catch (ASAPSecurityException | IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an ASAPSecurityException: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return  outputStream.toByteArray();
    }

    /**
     * Serializes a message object to a byte array message.
     * @param object    Object which nedds to be converted to a byte[].
     *
     * @return          The Object converted as byte[].
     */
    public static byte[] objectToByteArray(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(outputStream).writeObject(object);
            return outputStream.toByteArray();
        } catch (IOException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes an byte array message to a Message object.
     *
     * @param message    Received byte message.
     *
     * @return           The byte[] converted to an object.
     */
    public static Object byteArrayToObject(byte[] message) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        try {
            return new ObjectInputStream(inputStream).readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(Utilities.formattedTimestamp() + "Caught an Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
