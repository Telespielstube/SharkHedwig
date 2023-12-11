package Message;

import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPCryptoParameterStorage;
import net.sharksystem.asap.utils.ASAPSerialization;
import net.sharksystem.pki.SharkPKIComponent;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static Misc.Constants.APP_FORMAT;
import static Misc.Constants.PEER_NAME;

import Channel.Type;

public class MessageHandler implements IMessageHandler {

    private ASAPPeer peer;
    ASAPCryptoAlgorithms.EncryptedMessagePackage encryptedMessagePackage;
    private SharkPKIComponent sharkPKIComponent;
    private byte[] signedMessage;

    private byte[] decryptedMessage;
    private byte[] encryptedMessage;

    public MessageHandler() {}
    public MessageHandler(SharkPKIComponent sharkPKIComponent, ASAPPeer peer) {
        this.sharkPKIComponent = sharkPKIComponent;
        this.peer = peer;
    }

    public <T> T parseMessage(byte[] message, String senderE2E) {
        T object = null;
        try {
            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            this.decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(this.encryptedMessagePackage, this.sharkPKIComponent.getASAPKeyStore());
            if (ASAPCryptoAlgorithms.verify(this.decryptedMessage, message, senderE2E, sharkPKIComponent.getASAPKeyStore())) {
                object = (T) byteArrayToObject(this.decryptedMessage);
            }
        } catch (ASAPException | IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public <T> void buildOutgoingMessage(T object, Type uri, String recipient ) {
        byte[] unencryptedByteMessage = objectToByteArray(object);
        try {
            byte[] encryptedMessage = ASAPCryptoAlgorithms.produceEncryptedMessagePackage(unencryptedByteMessage, recipient , this.sharkPKIComponent.getASAPKeyStore());
            this.signedMessage = ASAPCryptoAlgorithms.sign(encryptedMessage ,this.sharkPKIComponent.getASAPKeyStore());
            this.peer.sendASAPMessage(APP_FORMAT, uri.toString(), this.signedMessage);
        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
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
