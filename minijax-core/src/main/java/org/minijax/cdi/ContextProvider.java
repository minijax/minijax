package org.minijax.cdi;

import org.minijax.MinijaxContextResolver;
import org.minijax.MinijaxRequestContext;

class ContextProvider<T> implements MinijaxProvider<T> {
    private final Key<T> key;

    public ContextProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    public T get(final MinijaxRequestContext context) {
        return MinijaxContextResolver.getContext(context, key.getType());
    }
}
