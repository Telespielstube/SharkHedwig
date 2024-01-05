package Message;

import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms.EncryptedMessagePackage;
import net.sharksystem.asap.utils.ASAPSerialization;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.*;

public class MessageHandler implements IMessageHandler {

    public MessageHandler() {}

    public Object parseMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        EncryptedMessagePackage encryptedMessagePackage;
        Object object = null;
        byte[] verifiedMessage;
        byte[] decryptedMessage;
        try {
            encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(encryptedMessagePackage, sharkPKIComponent.getASAPKeyStore());
            verifiedMessage = verifySignedMessage(decryptedMessage, senderE2E, sharkPKIComponent);
            object = byteArrayToObject(verifiedMessage);
        } catch (ASAPException | IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public byte[] buildOutgoingMessage(Object object, String uri, String recipient, SharkPKIComponent sharkPKIComponent) {
        byte[] byteMessage = objectToByteArray(object);
        byte[] encryptedMessage = new byte[0];
        byte[] signedMessage;
        try {
            signedMessage = composeSignedMessage(byteMessage, sharkPKIComponent);
            encryptedMessage = ASAPCryptoAlgorithms.produceEncryptedMessagePackage(signedMessage, recipient, sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
        return encryptedMessage;
    }

    private byte[] composeSignedMessage(byte[] byteMessage, SharkPKIComponent sharkPKIComponent) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] signedMessagePackage;
        try {
            signedMessagePackage = ASAPCryptoAlgorithms.sign(byteMessage, sharkPKIComponent.getASAPKeyStore());
            ASAPSerialization.writeByteArray(signedMessagePackage, outputStream);
            ASAPSerialization.writeByteArray(byteMessage, outputStream);
            signedMessagePackage = outputStream.toByteArray();
            outputStream.flush();
        } catch (ASAPSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        return signedMessagePackage;
    }

    private byte[] verifySignedMessage(byte[] decryptedSignedMessage, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptedSignedMessage);
        byte[] byteMessage;
        byte[] signedMessage[];
        try {
            byteMessage = ASAPSerialization.readByteArray(inputStream);
            signedMessage = ASAPSerialization.readByteArray(inputStream);
            ASAPCryptoAlgorithms.verify(signedMessage, byteMessage, senderE2E, sharkPKIComponent.getASAPKeyStore()
        } catch (ASAPException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] objectToByteArray(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    public Object byteArrayToObject(byte[] message) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        ObjectInputStream input;
        try {
            input = new ObjectInputStream(inputStream);
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
