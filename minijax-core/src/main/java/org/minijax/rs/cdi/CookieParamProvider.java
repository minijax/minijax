package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Cookie;

import org.minijax.rs.MinijaxRequestContext;

class CookieParamProvider<T> extends AbstractParamProvider<T> {

    public CookieParamProvider(final Class<T> type, final Annotation[] annotations, final String name, final DefaultValue defaultValue) {
        super(type, annotations, name, defaultValue);
    }

    @Override
    public String getStringValue(final MinijaxRequestContext ctx) {
        final Cookie cookie = ctx.getCookies().get(name);
        return cookie == null ? null : cookie.getValue();
    }
}
