package org.minijax.client;

import java.io.IOException;
import java.util.concurrent.Future;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

public class MinijaxClientInvocation implements jakarta.ws.rs.client.Invocation {
    private final MinijaxClient client;
    private final MinijaxClientHttpRequest httpRequest;

    public MinijaxClientInvocation(final MinijaxClient client, final MinijaxClientHttpRequest httpRequest) {
        this.client = client;
        this.httpRequest = httpRequest;
    }

    @Override
    public MinijaxClientResponse invoke() {
        try {
            return new MinijaxClientResponse(client.getHttpClient().execute(httpRequest));
        } catch (final IOException ex) {
            throw new WebApplicationException(ex);
        }
    }

    @Override
    public <T> T invoke(final Class<T> responseType) {
        try (final MinijaxClientResponse response = invoke()) {
            return response.readEntity(responseType);
        }
    }

    @Override
    public <T> T invoke(final GenericType<T> responseType) {
        try (final MinijaxClientResponse response = invoke()) {
            return response.readEntity(responseType);
        }
    }

    /*
     * Unsupported
     */

    @Override
    public Invocation property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future<Response> submit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> submit(final Class<T> responseType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> submit(final GenericType<T> responseType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> submit(final InvocationCallback<T> callback) {
        throw new UnsupportedOperationException();
    }
}
