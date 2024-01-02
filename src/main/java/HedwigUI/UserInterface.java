package HedwigUI;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * The UserInterface is more or less a placeholder class and defietly needs to be replaced by a more advaned mobile device
 * and desktop computer application. It simulates a user input and sends the verified data object to the protocol
 * intern ShippingLabel object for processing.
 */
public class UserInterface implements IUserInterface, Runnable {

    private String sender = null;
    private String origin = null;
    private String recipient = null;
    private double packageWeight = 0.0;
    private String destination = null;
    private double latitudeOrigin = 0.0;
    private double longitudeOrigin = 0.0;
    private double latitudeDest = 0.0;
    private double longitudeDest = 0.0;
    private IUserObserver userObserver;

    /**
     * Constructer prints out a helpful text on how to interact with the protocol.
     * @param infoText
     */
    public UserInterface(String infoText) {
        System.out.println(infoText);
    }

    public String readUserInput() {
        Scanner scanner = new Scanner((System.in));
        return scanner.next().trim().toLowerCase();
    }

    public void shippingLabelForm(String infoText) {
        DecimalFormat coordinates = new DecimalFormat("##.###");
        System.out.println(infoText);
        System.out.print("Sender: ");
        this.sender = readUserInput();
        System.out.print("Origin: ");
        this.origin = readUserInput();
        System.out.print("Longitude of the origin: ");
        this.longitudeOrigin = Double.parseDouble(coordinates.format(Double.parseDouble(readUserInput())));
        System.out.print("Latitude of the origin: ");
        this.latitudeOrigin = Double.parseDouble(coordinates.format(Double.parseDouble(readUserInput())));
        System.out.print("Recipient: ");
        this.recipient = readUserInput();
        System.out.print("Destination: ");
        this.destination = readUserInput();
        System.out.print("Longitude of the origin: ");
        this.longitudeDest = Double.parseDouble(coordinates.format(Double.parseDouble(readUserInput())));
        System.out.print("Latitude of the origin: ");
        this.latitudeDest = Double.parseDouble(coordinates.format(Double.parseDouble(readUserInput())));
        System.out.print("Package weight in grams: ");
        this.packageWeight = Float.parseFloat(readUserInput());
    }

    public void checkFormData(String userInfo) {
        System.out.println(userInfo);
        System.out.println("Sender:" + this.sender + "\nOrigin: " + this.origin + "\nLongitude of origin: " +
                this.longitudeOrigin + "\nLatitude of origin: " + this.latitudeOrigin + "\nRecipient: " +
                this.recipient +  "\nDestination: " + this.destination + "\nLongitude of destination: " +
                this.longitudeDest + "\nLatitude of destination: " + this.latitudeDest + "\nPackage weight: " +
                this.packageWeight);
    }

    public boolean acceptInput() {
        boolean accepted = false;
        String userInput;
        int counter = 0;
        System.out.println("Is the data correct? (yes/no): ");
        while (true) {
            userInput = readUserInput();
            if (userInput.equalsIgnoreCase("yes")) {
                accepted = true;
                break;
            }
            if (userInput.equalsIgnoreCase("no")) {
                break;
            } else {
                System.out.println("This is very important. You need to type 'yes' to proceed or 'no' to start over.");
                counter += 1;
                if (counter == 2) {
                    System.out.println("3 times wrong input.");
                    break;
                }
            }
        }
        return accepted;
    }

    @Override
    public void notifyUserObserver(UserInputBuilder userInputBuilder) {
        userObserver.userObjectCreated(userInputBuilder);
        
    }

    @Override
    public void addUserObserver() {
        userObserver = new UserObserver();
    }
    /**
     * Starts the user interface thread. This methode is overridden from the 'runnable' interface.
     */
    @Override
    public void run() {
        while (true) {
            String input = readUserInput();
            String cargo = "cargo";
            if (input.equalsIgnoreCase(cargo)) {
                shippingLabelForm("Shipping label. Please fill in the required information.");
                checkFormData("Please read very carefully because this is not reversible at a later date.");
                if (!acceptInput()) {
                    shippingLabelForm("Shipping label. Please fill in the required information.");
                }
                // saves the user input that the user interface does not access the shipping label directly.
                UserInputBuilder userInputBuilder = new UserInputBuilder(this.sender, this.origin, this.latitudeOrigin, this.longitudeOrigin,
                        this.recipient, this.destination, this.latitudeDest, this.longitudeDest,
                        this.packageWeight);
                notifyUserObserver(userInputBuilder);
                System.out.println("Shipping label created!");
            }
        }
    }
}
