package org.minijax.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

import org.minijax.rs.MinijaxHttpHeaders;

class MinijaxServletHttpHeaders extends MinijaxHttpHeaders {
    private final HttpServletRequest request;
    private MultivaluedMap<String, String> requestHeaders;
    private Map<String, Cookie> cookies;

    public MinijaxServletHttpHeaders(final HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public List<String> getRequestHeader(final String name) {
        final Enumeration<String> headers = request.getHeaders(name);
        return headers == null ? null : Collections.list(headers);
    }

    @Override
    public String getHeaderString(final String name) {
        return request.getHeader(name);
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        if (requestHeaders == null) {
            requestHeaders = new MultivaluedHashMap<>();
            final Enumeration<String> names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                final String name = names.nextElement();
                requestHeaders.put(name, Collections.list(request.getHeaders(name)));
            }

        }
        return requestHeaders;
    }

    @Override
    public Map<String, Cookie> getCookies() {
        if (cookies == null) {
            cookies = new HashMap<>();
            final jakarta.servlet.http.Cookie[] servletCookies = request.getCookies();
            for (jakarta.servlet.http.Cookie servletCookie : servletCookies) {
                final String name = servletCookie.getName();
                final String value = servletCookie.getValue();
                cookies.put(name, new Cookie(name, value));
            }
        }
        return cookies;
    }
}
