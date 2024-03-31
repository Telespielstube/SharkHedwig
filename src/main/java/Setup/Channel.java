package Setup;

/**
 * An enumeration of all channels the protocols needs to go through before a package is exchange.
 *
 * Ping                It is the entry point. A ping is sent to initiate the challenge-response authentication methode.
 *
 * Identification      Every exchange must be preceded by identification of the
 *                     two participating devices.
 * Request             The second step is the request. Message on this channel contain all attributes of the requesting
 *                     device important for devlicerng the packages
 * Handover            All mesages echanged on this channel contain highly sentive data regarding the package wexchange
 *                     between the two devices.
 *
 */
public enum Channel {
    NO_SESSION {
        @Override
        public String getChannel() {
            return (AppConstant.SCHEME.toString() + NO_SESSION).toLowerCase();
        }
    },
    REQUEST {
        @Override
        public String getChannel() {
            return (AppConstant.SCHEME.toString() + REQUEST).toLowerCase();
        }
    },
    CONTRACT {
        @Override
        public String getChannel() {
            return (AppConstant.SCHEME.toString() + CONTRACT).toLowerCase();
        }
    };

    public abstract String getChannel();
}