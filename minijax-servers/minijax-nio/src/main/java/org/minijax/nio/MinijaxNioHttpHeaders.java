package org.minijax.nio;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.minijax.rs.MinijaxHttpHeaders;

class MinijaxNioHttpHeaders extends MinijaxHttpHeaders {
    private final MultivaluedMap<String, String> requestHeaders;

    public MinijaxNioHttpHeaders(final MultivaluedMap<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public List<String> getRequestHeader(final String name) {
        return requestHeaders.get(name);
    }

    @Override
    public String getHeaderString(final String name) {
        return requestHeaders.getFirst(name);
    }
}
