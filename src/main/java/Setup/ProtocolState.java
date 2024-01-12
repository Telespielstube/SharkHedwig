package Setup;

/**
 * Enum class to set the role of the device. The initial state of the protocol is Transferee.
 * The state of the device changes to transferer if the user fills out a shipping label.
 * The second possibility of a change of state is when
 * the protocol hands over a carriage to a different device or destination.
 * This would change the state back to transferee.
 */
public enum ProtocolState {
    TRANSFEROR {

        @Override
        public ProtocolState isActive() {
            return TRANSFEROR;
        }
    },
    TRANSFEREE {
        @Override
        public ProtocolState isActive() {
            return TRANSFEREE;
        }
    };

    public abstract ProtocolState isActive();
}