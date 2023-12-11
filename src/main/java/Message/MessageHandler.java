package Message;

import Channel.Channel;
import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.asap.crypto.ASAPCryptoParameterStorage;
import net.sharksystem.asap.utils.ASAPSerialization;
import net.sharksystem.pki.SharkPKIComponent;

import java.io.*;
import java.security.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static Misc.Constants.APP_FORMAT;
import static Misc.Constants.PEER_NAME;

import Channel.Type;

public class MessageHandler implements IMessageHandler {

    private ASAPPeer peer;
    ASAPCryptoAlgorithms.EncryptedMessagePackage encryptedMessagePackage;
    private SharkPKIComponent sharkPKIComponent;
    private ByteArrayInputStream inputStream;
    private byte[] signedMessage;
    private byte[] deserialized;
    private byte[] decryptedMessage;
    private byte[] encryptedMessage;

    public MessageHandler() {}
    public MessageHandler(SharkPKIComponent sharkPKIComponent, ASAPPeer peer) {
        this.sharkPKIComponent = sharkPKIComponent;
        this.peer = peer;
    }

    public <T> void buildOutgoingMessage(T object, Type uri, String appName) {
        byte[] byteMessage = objectToByteArray(object);
        byte[] serializedMessage = serializeMessage(byteMessage, "PlaceholderForReceiver");
        encryptMessage(serializedMessage, "PlaceholderForReceiver");
        try {
            this.peer.sendASAPMessage(APP_FORMAT, uri, this.signedMessage);
        } catch (ASAPException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> byte[] objectToByteArray(T object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(object);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object byteArrayToObject(byte[] message) {
        this.inputStream = new ByteArrayInputStream(message);
        try {
            ObjectInputStream input = new ObjectInputStream(inputStream);
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] serializeMessage(byte[] byteMessage, CharSequence receiver) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Set<CharSequence> recipients = new HashSet<>();
        recipients.add(receiver);
        try {
            ASAPSerialization.writeByteArray(byteMessage, outputStream); // writes the content
            ASAPSerialization.writeCharSequenceParameter(PEER_NAME, outputStream); // writes the sender = PEER_NAME
            ASAPSerialization.writeCharSequenceSetParameter(recipients, outputStream); // recipient
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    private void encryptMessage(byte[] byteMessage, CharSequence receiver) {
        try {
            this.encryptedMessage = ASAPCryptoAlgorithms.produceEncryptedMessagePackage(byteMessage, receiver, sharkPKIComponent.getASAPKeyStore());
            this.signedMessage = ASAPCryptoAlgorithms.sign(this.encryptedMessage, sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public void decryptMessage(byte[] message) {
        try {
            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            this.decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(this.encryptedMessagePackage, this.sharkPKIComponent.getASAPKeyStore());
            ASAPCryptoAlgorithms.verify(this.decryptedMessage, this.sender, sharkPKIComponent.getPublicKey() );
        } catch (ASAPException | IOException e) {
            throw new RuntimeException(e);
        }
    }

        public CharSequence getReceiver() {
        return this.encryptedMessagePackage.getReceiver();
    }

    public byte[] deserializeMessage(byte[] serializedMessage)  {
        this.inputStream = new ByteArrayInputStream(serializedMessage);
        String sender;
        Set<CharSequence> receiverList;
        try {
            this.deserialized = ASAPSerialization.readByteArray(this.inputStream);
            this.sender = ASAPSerialization.readCharSequenceParameter(this.inputStream);
            receiverList = ASAPSerialization.readCharSequenceSetParameter(this.inputStream);
        } catch (IOException | ASAPException e) {
            throw new RuntimeException(e);
        }
        return this.deserialized;
    }
}
