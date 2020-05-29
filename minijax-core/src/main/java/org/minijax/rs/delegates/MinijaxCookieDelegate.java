package org.minijax.rs.delegates;

import java.net.HttpCookie;
import java.util.List;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

class MinijaxCookieDelegate implements HeaderDelegate<Cookie> {

    @Override
    @SuppressWarnings("squid:S3330") // Not a jakarta.servlet.http.Cookie
    public Cookie fromString(final String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        final List<HttpCookie> httpCookies = HttpCookie.parse(value);
        final HttpCookie httpCookie = httpCookies.get(0);
        return new Cookie(httpCookie.getName(), httpCookie.getValue(), httpCookie.getPath(), httpCookie.getDomain(), httpCookie.getVersion());
    }

    @Override
    @SuppressWarnings("squid:S3330") // Not a jakarta.servlet.http.Cookie
    public String toString(final Cookie cookie) {
        final HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
        httpCookie.setDomain(cookie.getDomain());
        httpCookie.setPath(cookie.getPath());
        return httpCookie.toString();
    }
}
