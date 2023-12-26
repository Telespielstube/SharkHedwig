package Message;

import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms.EncryptedMessagePackage;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.*;

public class MessageHandler implements IMessageHandler {

    public MessageHandler() {}

    public Object parseMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        EncryptedMessagePackage encryptedMessagePackage;
        Object object = null;
        byte[] decryptedMessage;
        try {
            encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(encryptedMessagePackage, sharkPKIComponent.getASAPKeyStore());
            if (ASAPCryptoAlgorithms.verify(decryptedMessage, message, senderE2E, sharkPKIComponent.getASAPKeyStore())) {
                object = byteArrayToObject(decryptedMessage);
            }
        } catch (ASAPException | IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public <T> byte[] buildOutgoingMessage(T object, String uri, String recipient, SharkPKIComponent sharkPKIComponent) {
        byte[] unencryptedByteMessage = objectToByteArray(object);
        byte[] encryptedMessage = new byte[0];
        byte[] signedMessage;
        try {
            signedMessage = ASAPCryptoAlgorithms.sign(unencryptedByteMessage, sharkPKIComponent.getASAPKeyStore());
            encryptedMessage = ASAPCryptoAlgorithms.produceEncryptedMessagePackage(signedMessage, recipient, sharkPKIComponent.getASAPKeyStore());

        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
        return encryptedMessage;
    }

    public <T> byte[] objectToByteArray(T object) {
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
