package Session.IdentificationSession;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import Message.Identification.AckMessage;
import Message.Identification.Response;
import Misc.Utilities;
import Message.IMessageHandler;
import Message.Identification.Challenge;
import Message.MessageFlag;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPPeer;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class IdentificationSession implements IIdentificationSession {

    private SharkPKIComponent sharkPKIComponent;
    private ASAPPeer peer;
    private String sender;
    private IMessageHandler messageHandler;
    private Challenge challenge;
    private Response response;
    private byte[] challengeNumber;
    private Cipher cipher;
    private byte[] encrypted;

    public IdentificationSession(String sender, SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.sender = sender;
        this.sharkPKIComponent = sharkPKIComponent;
        this.cipher = Cipher.getInstance("RSA");
    }

    @Override
    public Challenge initSession() {
        try {
            return new Challenge(this.challenge.createUUID(), Utilities.encryptRandomNumber(generateRandomNumber(), sharkPKIComponent.getPublicKey()),
                    MessageFlag.Challenge, Utilities.createTimestamp());
        } catch (ASAPSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean compare(byte[] decryptedNumber) {
        return Arrays.equals(challengeNumber, decryptedNumber);
    }

    public void processMessage(Object message) {
        if (((Response) message).getMessageFlag().equals(MessageFlag.Response.getFlag())) {
            Response response = (Response) message;
            if (compare(response.getDecryptedNumber())) {
//                return new Response(this.response.createUUID(), encryptRandomNumber() )
            }
        }
//
//        if (((AckMessage) message).getMessageFlag().equals(MessageFlag.Ack.getFlag())) {
//
//    }
    }

    public byte[] generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger bigInt = BigInteger.valueOf(secureRandom.nextInt());
        this.challengeNumber =  bigInt.toByteArray();
        return this.challengeNumber;
    }
}
