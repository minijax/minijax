package org.minijax.rs.test;

import java.util.List;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.minijax.rs.MinijaxHttpHeaders;

public class MinijaxTestHttpHeaders extends MinijaxHttpHeaders {
    private final MultivaluedMap<String, String> headers;

    public MinijaxTestHttpHeaders() {
        this(new MultivaluedHashMap<>());
    }

    public MinijaxTestHttpHeaders(final MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public List<String> getRequestHeader(final String name) {
        return headers.get(name);
    }

    @Override
    public String getHeaderString(final String name) {
        return headers.getFirst(name);
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        return headers;
    }
}
