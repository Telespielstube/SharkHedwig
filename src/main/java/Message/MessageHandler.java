package Message;

import Setup.AppConstant;
import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms.EncryptedMessagePackage;
import net.sharksystem.asap.utils.ASAPSerialization;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.*;

public class MessageHandler implements IMessageHandler {

    private EncryptedMessagePackage encryptedMessagePackage;

    public MessageHandler() {}

    public boolean checkRecipient(byte[] message) {
        try {
            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            if (!this.encryptedMessagePackage.getReceiver().equals(AppConstant.PEER_NAME.toString())) {
                return false;
            }
        } catch (IOException | ASAPException e) {
            System.err.println("Caught an ASAP or IOException: " + e);
            throw new RuntimeException(e);
        }
        return true;
    }

    public Object parseIncomingMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        Object object;
        byte[] verifiedMessage, decryptedMessage;
        try {
            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(this.encryptedMessagePackage, sharkPKIComponent.getASAPKeyStore());
            verifiedMessage = verifySignedMessage(decryptedMessage, senderE2E, sharkPKIComponent);
            object = byteArrayToObject(verifiedMessage);
        } catch (ASAPException | IOException e) {
            System.err.println("Caught an ASAP or IOException: " + e);
            throw new RuntimeException(e);
        }
        return object;
    }

    /**
     * Verifies the decrypted message by splitting the message part in 2 byte[]. Because verification needs the
     * signed message part and the unisgned message part to verify if the identity of the sender is corrent.
     *
     * @return    the verified byte[] message .
     */
    private byte[] verifySignedMessage(byte[] decryptedSignedMessage, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptedSignedMessage);
        byte[] byteMessage, signedMessage;
        try {
            byteMessage = ASAPSerialization.readByteArray(inputStream);
            signedMessage = ASAPSerialization.readByteArray(inputStream);
            if (ASAPCryptoAlgorithms.verify(signedMessage, byteMessage, senderE2E, sharkPKIComponent.getASAPKeyStore())) {
                return byteMessage;
            }
        } catch (ASAPException | IOException e) {
            System.err.println("Caught an ASAP or IOException: " + e);
            throw new RuntimeException(e);
        }
        return new byte[0];
    }

    public byte[] buildOutgoingMessage(Object object, String uri, String recipient,
                                       SharkPKIComponent sharkPKIComponent) {
        byte[] byteMessage = objectToByteArray(object);
        byte[] encryptedMessage;
        byte[] signedMessage;
        try {
            signedMessage = composeSignedMessage(byteMessage, sharkPKIComponent);
            encryptedMessage = ASAPCryptoAlgorithms.produceEncryptedMessagePackage(signedMessage, recipient,
                    sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPException e) {
            System.err.println("Caught an ASAPException: " + e);
            throw new RuntimeException(e);
        }
        return encryptedMessage;
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
            System.err.println("Caught an ASAPSecurityException: " + e);
            throw new RuntimeException(e);
        }
        return  outputStream.toByteArray();
    }

    public static byte[] objectToByteArray(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(object);
        } catch (IOException e) {
            System.err.println("Caught an IOException: " + e);
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    public static Object byteArrayToObject(byte[] message) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        ObjectInputStream input;
        try {
            input = new ObjectInputStream(inputStream);
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Caught an Exception: " + e);
            throw new RuntimeException(e);
        }
    }
}
