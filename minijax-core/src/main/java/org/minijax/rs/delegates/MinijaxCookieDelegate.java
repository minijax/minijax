package org.minijax.rs.delegates;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.minijax.rs.util.CookieUtils;

class MinijaxCookieDelegate implements HeaderDelegate<Cookie> {

    @Override
    public Cookie fromString(final String value) {
        return CookieUtils.parseCookie(value);
    }

    @Override
    public String toString(final Cookie cookie) {
        return CookieUtils.toString(cookie);
    }
}
