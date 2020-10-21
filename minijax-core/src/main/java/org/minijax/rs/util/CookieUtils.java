package org.minijax.rs.util;

import java.net.HttpCookie;
import java.util.List;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

public class CookieUtils {

    CookieUtils() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("squid:S3330") // Not a jakarta.servlet.http.Cookie
    public static Cookie parseCookie(final String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        final List<HttpCookie> httpCookies = HttpCookie.parse(value);
        final HttpCookie httpCookie = httpCookies.get(0);
        return new Cookie(httpCookie.getName(), httpCookie.getValue(), httpCookie.getPath(), httpCookie.getDomain(), httpCookie.getVersion());
    }

    @SuppressWarnings("squid:S3330") // Not a jakarta.servlet.http.Cookie
    public static String toString(final Cookie cookie) {
        final HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
        httpCookie.setDomain(cookie.getDomain());
        httpCookie.setPath(cookie.getPath());
        return httpCookie.toString();
    }

    @SuppressWarnings("squid:S3330") // Not a jakarta.servlet.http.Cookie
    public static NewCookie parseNewCookie(final String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        final List<HttpCookie> httpCookies = HttpCookie.parse(value);
        final HttpCookie httpCookie = httpCookies.get(0);
        return new NewCookie(
                httpCookie.getName(),
                httpCookie.getValue(),
                httpCookie.getPath(),
                httpCookie.getDomain(),
                httpCookie.getVersion(),
                httpCookie.getComment(),
                (int) httpCookie.getMaxAge(),
                null,
                httpCookie.getSecure(),
                httpCookie.isHttpOnly());
    }

    public static String toString(final NewCookie cookie) {
        final StringBuilder buf = new StringBuilder();
        buf.append(cookie.getName());
        buf.append('=');

        if (cookie.getValue() != null) {
            buf.append(cookie.getValue());
        }

        if (cookie.getPath() != null && cookie.getPath().length() > 0) {
            buf.append(";Path=").append(cookie.getPath());
        }

        if (cookie.getDomain() != null && cookie.getDomain().length() > 0) {
            buf.append(";Domain=").append(cookie.getDomain());
        }

        if (cookie.getMaxAge() >= 0) {
            buf.append(";Max-Age=");
            buf.append(cookie.getMaxAge());
        }

        if (cookie.isSecure()) {
            buf.append(";Secure");
        }

        if (cookie.isHttpOnly()) {
            buf.append(";HttpOnly");
        }

        return buf.toString();
    }
}
