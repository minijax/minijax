package org.minijax.rs;

import java.util.Date;
import java.util.List;

import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Variant;

public class MinijaxRequest implements Request {
    private final String method;

    public MinijaxRequest(final String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public Variant selectVariant(final List<Variant> variants) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder evaluatePreconditions(final EntityTag eTag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder evaluatePreconditions(final Date lastModified) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder evaluatePreconditions(final Date lastModified, final EntityTag eTag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseBuilder evaluatePreconditions() {
        throw new UnsupportedOperationException();
    }
}
