package Session;

/**
 * This is an enum to handle the multiple states of the protocoll. This is probably a nice methode to avoid
 * global variables, which are difficult to handle and which we want to avoid with a primary focus on a secure protocol.
 */
public enum SessionState {

    NoState {
        @Override
        public boolean getSesssionState() {
            return true;
        }
    },

    Identification {
        @Override
        public boolean getSesssionState() {
            return true;
        }
    },

    Request {
        @Override
        public boolean getSesssionState() {
            return true;
        }
    },

    HndoverSate() {
        @Override
        public boolean getSesssionState() {
            return true;
        }
    };

    public abstract boolean getSesssionState();
}
