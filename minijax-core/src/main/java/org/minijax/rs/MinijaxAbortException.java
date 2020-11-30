package org.minijax.rs;

import java.util.Objects;

import jakarta.ws.rs.core.Response;

class MinijaxAbortException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Response response;

    public MinijaxAbortException(final Response response) {
        this.response = Objects.requireNonNull(response);
    }

    public Response getResponse() {
        return response;
    }
}
