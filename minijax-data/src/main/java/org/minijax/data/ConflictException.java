package org.minijax.data;

/**
 * The ConflictException represents a HTTP 409 error.
 */
public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_MESSAGE = "Conflict";
    private final String key;
    private final String value;

    public ConflictException() {
        super(DEFAULT_MESSAGE);
        key = null;
        value = null;
    }

    public ConflictException(final String key, final String value) {
        super("The " + key + " '" + value + "' already exists");
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
