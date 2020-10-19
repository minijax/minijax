package org.minijax.rs.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.minijax.rs.MinijaxHttpHeaders;

public class MinijaxTestHttpHeaders extends MinijaxHttpHeaders {
    private final MultivaluedMap<String, String> headers;
    private final Map<String, Cookie> cookies;

    public MinijaxTestHttpHeaders() {
        this(new MultivaluedHashMap<>(), new HashMap<>());
    }

    public MinijaxTestHttpHeaders(final MultivaluedMap<String, String> headers, final Map<String, Cookie> cookies) {
        this.headers = headers;
        this.cookies = cookies;
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

    @Override
    public Map<String, Cookie> getCookies() {
        return cookies;
    }
}
