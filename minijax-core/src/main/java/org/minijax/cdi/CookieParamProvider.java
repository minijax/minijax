package org.minijax.cdi;

import javax.inject.Provider;
import javax.ws.rs.core.Cookie;

import org.minijax.MinijaxRequestContext;

class CookieParamProvider<T> implements Provider<T> {
    private final Key<T> key;

    public CookieParamProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    public T get() {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        final Cookie cookie = context.getCookies().get(key.getName());
        final String cookieValue = cookie == null ? null : cookie.getValue();
        return context.getContainer().convertStringToType(cookieValue, key.getType());
    }
}
