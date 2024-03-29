package Session;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.NoSuchPaddingException;

import Battery.*;
import Location.GeoSpatial;
import Location.IGeoSpatial;
import Message.Message;
import Message.Authentication.Ack;
import Message.Advertisement;
import Message.IMessage;
import Message.Authentication.*;
import Message.Request.Offer;
import Misc.Utilities;
import Message.MessageFlag;
import Setup.AppConstant;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.crypto.ASAPCryptoAlgorithms;
import net.sharksystem.pki.SharkPKIComponent;

public class Authentication extends AbstractSession {

    private IBattery battery;
    private SharkPKIComponent sharkPKIComponent;
    private byte[] secureNumber;
    private Optional<Message> optionalMessage;
    private IGeoSpatial geoSpatial;

    public Authentication(){}
    /**
     * Identification constructor
     *
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public Authentication(SharkPKIComponent sharkPKIComponent) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.sharkPKIComponent = sharkPKIComponent;
        this.optionalMessage = Optional.empty();
        this.geoSpatial = new GeoSpatial();
        this.battery = new Battery();
        this.secureNumber = new byte[0];
    }

    @Override
    public Optional<Object> transferor(IMessage message, String sender) {
        switch(message.getMessageFlag()) {
            case ADVERTISEMENT:
                handleAdvertisement((Advertisement) message);
                break;
            case RESPONSE:
                handleResponse((Response) message);
                break;
            case ACK:
                handleAck((Ack) message);
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(this.optionalMessage.get());
        }
        return Optional.of(this.optionalMessage.get());
    }

    @Override
    public Optional<Object> transferee(IMessage message, String sender) {
        switch(message.getMessageFlag()) {
            case CHALLENGE:
                handleChallenge((Challenge) message);
                break;
            case RESPONSE_REPLY:
                handleResponseReply((Response) message);
                break;
            case READY:
                handleAck((Ack) message);
                break;
            default:
                System.err.println("Message flag was incorrect: " + message.getMessageFlag());
                clearMessageList();
                break;
        }
        if (!this.optionalMessage.isPresent()) {
            clearMessageList();
        } else {
            addMessageToList(this.optionalMessage.get());
        }
        return Optional.of(this.optionalMessage.get());
    }

    /**
     * Handles a received Advertisement message.
     *
     * @param message    Advertisement message.
     *
     * @return           if message was valid a Challenge message, if not an empty Optional.
     */
    private void handleAdvertisement(Advertisement message) {
        if (message.getAdTag()) {
            try {
                this.secureNumber = generateRandomNumber();
                this.optionalMessage = Optional.of(new Challenge(Utilities.createUUID(), MessageFlag.CHALLENGE,
                        Utilities.createTimestamp(), Utilities.encryptAsymmetric(this.secureNumber,
                        this.sharkPKIComponent.getPublicKey())));

            } catch (ASAPSecurityException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Processes the steaps after receiveing the Response message bt compares decryptedNumber to the encryptedNumber
     * in the snt Challenge Message objtect and decrypts the received encrypted number from the transferee.
     *
     * @param response    Received Response object.
     * @return            Response object.
     */
    private void handleResponse(Response response) {
        if ( compareTimestamp(response.getTimestamp(), timeOffset) && compareDecryptedNumber(response.getDecryptedNumber()) ) {
            try{
                byte[] decryptedNumber = ASAPCryptoAlgorithms.decryptAsymmetric(response.getChallengeNumber(), this.sharkPKIComponent.getASAPKeyStore());
                this.optionalMessage = Optional.of(new Response(Utilities.createUUID(), MessageFlag.RESPONSE_REPLY,
                        Utilities.createTimestamp(), decryptedNumber));
            } catch (ASAPSecurityException e) {
                System.err.println("Caught an ASAPSecurityException: " + e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * An Acknowledgment massage to signal that the second secure code was decrypted successfully. It compares the timestamp
     * checks the flag and checks if the last TreeMap entry equals Ack.
     *
     * @param message    The received AckMessage object.
     * @return              An Optional AckMessage object if timestamp and ack flag are ok
     *                      or an empty Optional if it's not.
     */
    private void handleAck(Ack message) {
        if (compareTimestamp(message.getTimestamp(), timeOffset) && message.getIsAck()) {
            if (message.getMessageFlag().equals(MessageFlag.ACK)) {
                this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.READY,
                        Utilities.createTimestamp(), true));
                this.sessionComplete = true;
            } else if (message.getMessageFlag().equals(MessageFlag.READY)) {
                this.sessionComplete = true;
                this.optionalMessage = Optional.of(new Offer(Utilities.createUUID(), MessageFlag.OFFER,
                        Utilities.createTimestamp(), battery.maxFlightRange(), AppConstant.APP_FORMAT.getInt(), this.geoSpatial.getCurrentLocation() ));
            }
        }
    }

    /**
     * After receiving the Challenge message the Transfere neds to create a Response message object.
     *
     * @param message    Challenge message object.
     *
     * @return           Response message object, or an empty Optional container.
     */

    private void handleChallenge(Challenge message) {
        if (message.getChallengeNumber().length > 0) {
            try {
                byte[] decryptedNumber = ASAPCryptoAlgorithms.decryptAsymmetric(message.getChallengeNumber(), this.sharkPKIComponent.getASAPKeyStore());
                this.optionalMessage = Optional.of(new Response(Utilities.createUUID(), MessageFlag.RESPONSE,
                        Utilities.createTimestamp(), Utilities.encryptAsymmetric(generateRandomNumber(),
                        this.sharkPKIComponent.getPublicKey()), decryptedNumber));
            } catch (ASAPSecurityException e) {
                System.err.println("Caught an ASAPSecurityException: " + e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The Challenge initiator has to solve a challenge from the Transferee too. This is the Cahllenge reply from the Transferor
     *
     * @param responseReply    ResponseRelpy message object.
     *
     * @return                 Optional AckMessage object or empty Optional.
     */
    private void handleResponseReply(Response message) {
        if ( compareTimestamp(message.getTimestamp(), timeOffset) && compareDecryptedNumber(message.getDecryptedNumber()) ) {
            this.optionalMessage = Optional.of(new Ack(Utilities.createUUID(), MessageFlag.ACK, Utilities.createTimestamp(), true));
        }
    }

    /**
     * Generates a sceure random number. The SecureRandom class generates a number up to 128 bits.
     * Makes the chance of repeating smaller.
     *
     * @return    The secure random number converted to byte[] ready for encryption.
     */
    private byte[] generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[10];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    /**
     * Compares two decrypted secure random numbers to check if the message sender's identitiy is corrent.
     *
     * @param decryptedNumber    Received decrypted number. This is the securely generated Number from the Challenge object.
     * @return                   True if decrypted number matches the saved number.
     */

    private boolean compareDecryptedNumber(byte[] decryptedNumber) {
        return Arrays.equals(this.secureNumber, decryptedNumber);
    }
}
