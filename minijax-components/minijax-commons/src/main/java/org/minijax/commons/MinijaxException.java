package org.minijax.commons;

public class MinijaxException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MinijaxException(final String message) {
        super(message);
    }

    public MinijaxException(final Throwable cause) {
        super(cause);
    }

    public MinijaxException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
