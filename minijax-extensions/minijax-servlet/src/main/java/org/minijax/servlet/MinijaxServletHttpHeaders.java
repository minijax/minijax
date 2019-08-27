package org.minijax.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.minijax.MinijaxHttpHeaders;

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
            final javax.servlet.http.Cookie[] servletCookies = request.getCookies();
            for (int i = 0; i < servletCookies.length; i++) {
                final String name = servletCookies[i].getName();
                final String value = servletCookies[i].getValue();
                cookies.put(name, new Cookie(name, value));
            }
        }
        return cookies;
    }
}
