package Setup;

/**
 * Enum class to set the role of the device. The initial state of the protocol is Transferee.
 * The state of the device changes to transferer if the user fills out a shipping label.
 * The second possibility of a change of state is when
 * the protocol hands over a carriage to a different device or destination.
 * This would change the state back to transferee.
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