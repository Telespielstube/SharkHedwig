package Message;

import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;
import java.io.*;
import Channel.*;

public class MessageHandler implements IMessageHandler {

    private ASAPPeer peer;
    ASAPCryptoAlgorithms.EncryptedMessagePackage encryptedMessagePackage;
    private SharkPKIComponent sharkPKIComponent;
    private byte[] signedMessage;

    private byte[] decryptedMessage;
    private byte[] encryptedMessage;

    public MessageHandler() {}

    public <T> T parseMessage(byte[] message, String senderE2E, SharkPKIComponent sharkPKIComponent) {
        T object = null;
        try {
            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            this.decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(this.encryptedMessagePackage, sharkPKIComponent.getASAPKeyStore());
            if (ASAPCryptoAlgorithms.verify(this.decryptedMessage, message, senderE2E, sharkPKIComponent.getASAPKeyStore())) {
                object = (T) byteArrayToObject(this.decryptedMessage);
            }
        } catch (ASAPException | IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public <T> byte[] buildOutgoingMessage(T object, String uri, String recipient ) {
        byte[] unencryptedByteMessage = objectToByteArray(object);
        try {
            this.encryptedMessage = ASAPCryptoAlgorithms.produceEncryptedMessagePackage(unencryptedByteMessage, recipient, this.sharkPKIComponent.getASAPKeyStore());
            this.signedMessage = ASAPCryptoAlgorithms.sign(this.encryptedMessage, this.sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
        return this.signedMessage;
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
    public <T> boolean compareTimestamp(T object1, T object2) {
        boolean passed = false;
        return true;
    }
}
