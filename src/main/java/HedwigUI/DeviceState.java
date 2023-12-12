package HedwigUI;

/**
 * Enum class to set the role of the device. The initial state of the protocol is Transferee.
 * The state of the device changes if the user fills out a shipping label.
 * Then the device changes its state to Transferor. The second possibility of a state change is when
 * the protocol hands over a carriage to a different device or destination.
 */
public enum DeviceState {
    Transferor {
        public boolean setTransferorState(boolean isTransferor) {
            return true;
        }

        @Override
        public boolean getCurrentState() {
            return true;
        }
    },
    Transferee {
        @Override
        public boolean setTransferorState(boolean isTransferor) {
            return false;
        }

        @Override
        public boolean getCurrentState() {
            return false;
        }
    };

    public abstract boolean setTransferorState(boolean isTransferor);
    public abstract boolean getCurrentState();
}
