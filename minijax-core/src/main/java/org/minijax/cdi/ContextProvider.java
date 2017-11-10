package org.minijax.cdi;

import javax.inject.Provider;

import org.minijax.MinijaxRequestContext;

class ContextProvider<T> implements Provider<T> {
    private final Key<T> key;

    public ContextProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    public T get() {
        final Class<T> c = key.getType();
        return MinijaxRequestContext.getThreadLocal()
                .getApplication()
                .getProviders()
                .getContextResolver(c, null)
                .getContext(c);
    }
}
