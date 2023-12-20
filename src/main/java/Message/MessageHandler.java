package Message;

import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.*;

public class MessageHandler implements IMessageHandler {

    private ASAPPeer peer;
    private SharkPKIComponent sharkPKIComponent;


    public MessageHandler() {}

    public <T> T parseMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        ASAPCryptoAlgorithms.EncryptedMessagePackage encryptedMessagePackage;
        T object = null;
        byte[] decryptedMessage;

        try {
            encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(encryptedMessagePackage, sharkPKIComponent.getASAPKeyStore());
            if (ASAPCryptoAlgorithms.verify(decryptedMessage, message, senderE2E, sharkPKIComponent.getASAPKeyStore())) {
                object = (T) byteArrayToObject(decryptedMessage);
            }
        } catch (ASAPException | IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public <T> byte[] buildOutgoingMessage(T object, String uri, String recipient) {
        byte[] unencryptedByteMessage = objectToByteArray(object);
        byte[] signedMessage;
        byte[] encryptedMessage;
        try {
            encryptedMessage = ASAPCryptoAlgorithms.produceEncryptedMessagePackage(unencryptedByteMessage, recipient, this.sharkPKIComponent.getASAPKeyStore());
            signedMessage = ASAPCryptoAlgorithms.sign(encryptedMessage, this.sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
        return signedMessage;
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
