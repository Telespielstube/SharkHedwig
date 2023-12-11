package Message;

import net.sharksystem.asap.*;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
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

    public Object parseMessage(byte[] message, String senderE2E) {
        Object object = null;
        try {
            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
            this.decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(this.encryptedMessagePackage, this.sharkPKIComponent.getASAPKeyStore());
            if (ASAPCryptoAlgorithms.verify(this.decryptedMessage, message, senderE2E, sharkPKIComponent.getASAPKeyStore())) {
                object = byteArrayToObject(this.decryptedMessage);
            }
        } catch (ASAPException | IOException e) {
            throw new RuntimeException(e);
        }
        return object;
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
   //     try {
 //           if (!decryptMessage(message, senderE2E)) {

//            } else {
//                this.deserialized = this.messageHandler.deserializeMessage();
//            }
//        } catch (ASAPException e) {
//            throw new RuntimeException(e);
//        }
//        if (this.sharkPKIComponent.isOwner(this.messageHandler.getReceiver())) {
//
//        } catch (ASAPSecurityException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ASAPException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    public boolean decryptMessage(byte[] message, String senderE2E) {
//        boolean verified = false;
//        try {
//            this.encryptedMessagePackage = ASAPCryptoAlgorithms.parseEncryptedMessagePackage(message);
//            this.decryptedMessage = ASAPCryptoAlgorithms.decryptPackage(this.encryptedMessagePackage, this.sharkPKIComponent.getASAPKeyStore());
//            if (ASAPCryptoAlgorithms.verify(this.decryptedMessage, message , senderE2E, sharkPKIComponent.getASAPKeyStore())) {
//                verified = true;
//            }
//        } catch (ASAPException | IOException e) {
//            throw new RuntimeException(e);
//        }
//        return verified;
//    }

//    public byte[] deserializeMessage(byte[] next)  {
//        byte[] deserializedContent;
//        this.inputStream = new ByteArrayInputStream(this.decryptedMessage);
//        String sender;
//        Set<CharSequence> receiverList;
//        try {
//            deserializedContent = ASAPSerialization.readByteArray(this.inputStream);
//            sender = ASAPSerialization.readCharSequenceParameter(this.inputStream);
//            receiverList = ASAPSerialization.readCharSequenceSetParameter(this.inputStream);
//
//        } catch (IOException | ASAPException e) {
//            throw new RuntimeException(e);
//        }
//        return this.deserialized;
//    }

    public <T> void buildOutgoingMessage(T object, Type uri, String appName) {
        byte[] byteMessage = objectToByteArray(object);
        byte[] serializedMessage = serializeMessage(byteMessage, "PlaceholderForReceiver");
        encryptMessage(serializedMessage, "PlaceholderForReceiver");
        try {
            this.peer.sendASAPMessage(APP_FORMAT, uri.toString(), this.signedMessage);
        } catch (ASAPException e) {
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

}
