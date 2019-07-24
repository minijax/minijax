package org.minijax.db;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 * The ConflictException represents a HTTP 409 error.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class ConflictException extends ClientErrorException {
    private static final long serialVersionUID = 1L;
    private final String key;
    private final String value;

    public ConflictException(final String key, final String value) {
        super("The " + key + " '" + value + "' already exists", Response.Status.CONFLICT);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    @Deprecated
    public String getValue() {
        return value;
    }
}
