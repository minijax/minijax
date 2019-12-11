package org.minijax.util;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * The IdUtils class provides helper methods for creating, parsing, and using UUID's.
 */
public class IdUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Pattern PATTERN = Pattern.compile("[0-9a-fA-F\\-]+");


    IdUtils() {
        throw new UnsupportedOperationException();
    }


    /**
     * Creates a new UUID.
     *
     * These UUID's do not follow the official UUID specification.
     *
     * The use the following format:
     *   1) 6 bytes of time stamp, milliseconds since the epoch
     *   2) 10 bytes of randomness
     *
     * This provides a mostly time-sortable UUID, which is much better for
     * database clustering.
     *
     * @return A new UUID.
     */
    public static UUID create() {
        final byte[] bytes = new byte[16];

        // Fill with random
        RANDOM.nextBytes(bytes);

        // Set the first 6 bytes / 48 bits to time stamp
        final long time = System.currentTimeMillis();
        for (int i = 0; i < 6; i++) {
            bytes[i] = (byte) ((time >> (40 - i * 8)) & 0xFF);
        }

        return fromBytes(bytes);
    }


    /**
     * Tries to parse a UUID.
     *
     * Unlike the built-in <code>UUID.fromString()</code> method,
     * this method is more forgiving.  It ignores all dashes.
     * It returns null on failure rather than throwing an exception.
     *
     * @param str The input string.
     * @return A UUID on success; null on failure.
     */
    public static UUID tryParse(final String str) {
        if (str == null) {
            return null;
        }

        if (!PATTERN.matcher(str).matches()) {
            return null;
        }

        final String clean = str.replace("-", "");
        if (clean.length() != 32) {
            return null;
        }

        final long msb = Long.parseUnsignedLong(clean.substring(0, 16), 16);
        final long lsb = Long.parseUnsignedLong(clean.substring(16), 16);
        return new UUID(msb, lsb);
    }


    /**
     * Converts a UUID to a 16-element byte array.
     *
     * @param id The UUID.
     * @return The 16-element byte array.
     */
    public static byte[] toBytes(final UUID id) {
        if (id == null) {
            return null; // NOSONAR - Must be null for JDBC
        }

        final byte[] buffer = new byte[16];
        final ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.putLong(id.getMostSignificantBits());
        bb.putLong(id.getLeastSignificantBits());
        return buffer;
    }


    /**
     * Converts a byte array to a UUID.
     *
     * @param b The byte array.
     * @return The new UUID.
     */
    public static UUID fromBytes(final byte[] b) {
        if (b == null || b.length != 16) {
            return null;
        }

        final ByteBuffer bb = ByteBuffer.wrap(b);
        return new UUID(bb.getLong(), bb.getLong());
    }
}
