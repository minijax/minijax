package org.minijax.cdi;

import org.minijax.MinijaxRequestContext;

class HeaderParamProvider<T> implements MinijaxProvider<T> {
    private final Key<T> key;

    public HeaderParamProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    public T get(final MinijaxRequestContext context) {
        return context.getApplicationContext().convertParamToType(context.getHeaderString(key.getName()), key.getType(), key.getAnnotations());
    }
}
