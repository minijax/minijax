package org.minijax.rs.delegates;

import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.minijax.rs.util.CookieUtils;

class MinijaxNewCookieDelegate implements HeaderDelegate<NewCookie> {

    @Override
    public NewCookie fromString(final String value) {
        return CookieUtils.parseNewCookie(value);
    }

    @Override
    public String toString(final NewCookie cookie) {
        return CookieUtils.toString(cookie);
    }
}
