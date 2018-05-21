package org.minijax.util;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * The NetworkUtils class provides static utility helpers for network related tasks.
 */
public class NetworkUtils {

    NetworkUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the preferred IP address for a given collection of network interfaces.
     *
     * Ignores IPv6 addresses, loopback addresses (127.), and link local addresses (169.).
     *
     * @param nis The collection of network interfaces.
     * @return The preferred IP address if available; null otherwise.
     */
    public static String getIpAddress(final Enumeration<NetworkInterface> nis) {
        while (nis.hasMoreElements()) {
            final NetworkInterface ni = nis.nextElement();
            final String address = getIpAddress(ni);
            if (address != null && !address.startsWith("127.") && !address.startsWith("169.")) {
                return address;
            }
        }
        return null;
    }


    /**
     * Returns the preferred IP address for a network interface.
     *
     * @param ni The network interface.
     * @return The preferred IP address if available; null otherwise.
     */
    private static String getIpAddress(final NetworkInterface ni) {
        for (final InterfaceAddress ia : ni.getInterfaceAddresses()) {
            final String address = ia.getAddress().getHostAddress();
            if (address.indexOf(':') < 0) {
                return address;
            }
        }
        return null;
    }
}
