package org.minijax.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * The AuthUtils class provides helper methods for parsing the
 * HTTP Authorization header.
 */
public class AuthUtils {
    private static final String BASIC_PREFIX = "Basic ";
    private static final String[] INVALID = new String[0];


    AuthUtils() {
        throw new UnsupportedOperationException();
    }


    public static String create(final String username, final String password) {
        return BASIC_PREFIX + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }


    /**
     * Returns the username portion of a Basic Authentication header.
     * Returns null if malformed.
     *
     * @param auth The Basic Authentication header.
     * @return The username portion or null if not available.
     */
    public static String getUsername(final String auth) {
        final String[] components = getUsernamePassword(auth);
        return components == INVALID ? null : components[0];
    }


    /**
     * Returns the password portion of a Basic Authentication header.
     * Returns null if malformed.
     *
     * @param auth The Basic Authentication header.
     * @return The password portion or null if not available.
     */
    public static String getPassword(final String auth) {
        final String[] components = getUsernamePassword(auth);
        return components == INVALID ? null : components[1];
    }


    /**
     * Returns the username and password from a Basic Authentication header.
     *
     * @param auth The Basic Authentication header.
     * @return Either INVALID or a 2-item array.
     */
    private static String[] getUsernamePassword(final String auth) {
        if (auth == null || !auth.startsWith(BASIC_PREFIX)) {
            // No Authorization header present
            // or not Basic authentication
            return INVALID;
        }

        final byte[] decodedBytes;
        try {
            decodedBytes = Base64.getDecoder().decode(auth.substring(BASIC_PREFIX.length()));
        } catch (final IllegalArgumentException ex) {
            return INVALID;
        }

        final String decoded = new String(decodedBytes, StandardCharsets.UTF_8);
        final int colonIndex = decoded.indexOf(':');
        if (colonIndex == -1) {
            // No colon means this is a malformed header.
            return INVALID;
        }

        return decoded.split(":", 2);
    }
}
