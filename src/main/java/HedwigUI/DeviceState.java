package HedwigUI;

/**
 * Enum class to set the role of the device. The initial state of the protocol is Transferee.
 * The state of the device changes if the user fills out a shipping label.
 * Then the device changes its state to Transferor. The second possibility of a state change is when
 * the protocol hands over a carriage to a different device or destination.
 */
public enum DeviceState {
    Transferor {

        @Override
        public DeviceState isActive() {
            return Transferor;
        }
        @Override
        public DeviceState isInactive() {
            return Transferee;
        }

    },
    Transferee {
        @Override
        public DeviceState isActive() {
            return Transferee;
        }

        @Override
        public DeviceState isInactive() {
            return Transferor;
        }
    };

    public abstract DeviceState isActive();
    public abstract DeviceState isInactive();

}