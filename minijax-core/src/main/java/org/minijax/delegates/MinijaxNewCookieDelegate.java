package org.minijax.delegates;

import java.net.HttpCookie;
import java.util.List;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

class MinijaxNewCookieDelegate implements HeaderDelegate<NewCookie> {

    @Override
    @SuppressWarnings("squid:S3330") // Not a javax.servlet.http.Cookie
    public NewCookie fromString(final String value) {
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

    @Override
    public String toString(final NewCookie cookie) {
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
