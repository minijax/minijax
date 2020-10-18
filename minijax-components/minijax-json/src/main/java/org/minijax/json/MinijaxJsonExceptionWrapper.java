package org.minijax.json;

import jakarta.ws.rs.WebApplicationException;

public class MinijaxJsonExceptionWrapper {
    private final int code;
    private final String message;

    private MinijaxJsonExceptionWrapper(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public MinijaxJsonExceptionWrapper(final WebApplicationException ex) {
        this(ex.getResponse().getStatus(), ex.getMessage());
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
