package org.minijax.rs.cdi;

import org.minijax.cdi.MinijaxProvider;
import org.minijax.rs.MinijaxContextResolver;
import org.minijax.rs.MinijaxRequestContext;

class ContextProvider<T> implements MinijaxProvider<T> {
    private final Class<T> contextType;

    public ContextProvider(final Class<T> contextType) {
        this.contextType = contextType;
    }

    @Override
    public T get(final Object context) {
        return MinijaxContextResolver.getContext((MinijaxRequestContext) context, contextType);
    }
}
