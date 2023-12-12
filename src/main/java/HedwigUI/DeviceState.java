package HedwigUI;

/**
 * Enum class to set the role of the device. The initial state of the protocol is Transferee.
 * The state of the device changes if the user fills out a shipping label.
 * Then the device changes its state to Transferor. The second possibility of a state change is when
 * the protocol hands over a carriage to a different device or destination.
 */
public enum DeviceState {
    Transferor {
        public boolean isActive() {
            return true;
        }
        public boolean isInactive() {
            return false;
        }
    },
    Transferee {
        public boolean isActive() {
            return true;
        }
        public boolean isInactive() {
            return false;
        }
    };

    public abstract boolean isActive();
    public abstract boolean isInactive();

}