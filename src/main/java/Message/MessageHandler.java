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
    private SharkPKIComponent sharkPKIComponent;
    private ByteArrayInputStream inputStream;
    private Object receivedMessageObject;

    public MessageHandler() {}
    public MessageHandler(SharkPKIComponent sharkPKIComponent, ASAPPeer peer) {
        this.sharkPKIComponent = sharkPKIComponent;
        this.peer = peer;
    }

    public void handleIncomingMessage(byte[] message) {
        byte[] decryptedMessage = decryptMessage(message);
        byte[] deserializedMessage = deserializeMessage(decryptedMessage);
        this.receivedMessageObject = byteArrayToObject(deserializedMessage);
    }

    public <T> void buildOutgoingMessage(T object, Type uri, String appName) {
        byte[] byteMessage = objectToByteArray(object);
        byte[] serializedMessage = serializeMessage(byteMessage, "Placeholder");
       // this.peer.sendASAPMessage(APP_FORMAT, uri, encryptedMessage);
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

    public byte[] signMessage(byte[] unsignedMessage) {
        byte[] signedMessage;
        try {
            signedMessage = ASAPCryptoAlgorithms.sign(unsignedMessage, sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPSecurityException e) {
            throw new RuntimeException(e);
        }
        return signedMessage;
    }

    public boolean verifySignature(byte[] signedMessage, byte[] signatureToBeVerified) {
        boolean verified = false;
        try {
            Signature signature = Signature.getInstance(ASAPCryptoParameterStorage.DEFAULT_ASYMMETRIC_SIGNATURE_ALGORITHM);
            signature.initVerify(sharkPKIComponent.getPublicKey());
            signature.update(signedMessage);
            verified = signature.verify(signatureToBeVerified);
        } catch (NoSuchAlgorithmException | ASAPSecurityException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
        return verified;
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

    public byte[] encryptMessage(byte[] byteMessage, CharSequence receiver) {
        try {
            return ASAPCryptoAlgorithms.produceEncryptedMessagePackage(byteMessage, receiver, sharkPKIComponent.getASAPKeyStore());
        } catch (ASAPSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decryptMessage(byte[] message) {
        ASAPCryptoAlgorithms.EncryptedMessagePackage encryptedMessagePackage;
        byte[] decryptedMessage = null;
        try {
            encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            if (!sharkPKIComponent.isOwner(encryptedMessagePackage.getReceiver())) {

            }
            decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(encryptedMessagePackage, sharkPKIComponent);
        } catch (IOException | ASAPException e) {
            throw new RuntimeException(e);
        }
        return decryptedMessage;
    }

    public byte[] deserializeMessage(byte[] serializedMessage)  {
        this.inputStream = new ByteArrayInputStream(serializedMessage);
        byte[] messageContent = null;
        String sender;
        Set<CharSequence> receiverList;
        try {
            messageContent = ASAPSerialization.readByteArray(this.inputStream);
            sender = ASAPSerialization.readCharSequenceParameter(this.inputStream);
            receiverList = ASAPSerialization.readCharSequenceSetParameter(this.inputStream);
        } catch (IOException | ASAPException e) {
            throw new RuntimeException(e);
        }
        return messageContent;
    }
}
