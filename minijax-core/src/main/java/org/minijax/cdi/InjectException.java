package org.minijax.cdi;

class InjectException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InjectException(final String message) {
        super(message);
    }

    public InjectException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
