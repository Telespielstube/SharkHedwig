package Channel;

/**
 * An enumeration of all channels the protocols needs to go trough before a package is exchange.
 *
 * IDENTIFICATION      Its the entry point to the protocol. Every exchange must be preceded by identification of the
 *                     two participating devices.
 * REQUEST             The second step is the request. Message on this channel contain all attributes of the requesting
 *                     device important for devlicerng the packages
 * HANDOVER            All mesages echanged on this channel contain highly sentive data regarding the package wexchange
 *                     between the two devices.
 *
 */
public enum Type {
    REQUEST,
    HANDOVER,
    IDENTIFICATION
}

