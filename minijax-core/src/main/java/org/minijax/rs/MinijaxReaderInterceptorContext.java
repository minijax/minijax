package org.minijax.rs;

import java.io.IOException;
import java.io.InputStream;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.ReaderInterceptorContext;

public class MinijaxReaderInterceptorContext extends MinijaxInterceptorContext implements ReaderInterceptorContext {
    private final MinijaxRequestContext ctx;
    private InputStream inputStream;

    public MinijaxReaderInterceptorContext(final MinijaxRequestContext ctx, final InputStream inputStream) {
        this.ctx = ctx;
        this.inputStream = inputStream;
    }

    public MinijaxRequestContext getContext() {
        return ctx;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object proceed() throws IOException, WebApplicationException {
        throw new UnsupportedOperationException();
    }
}
