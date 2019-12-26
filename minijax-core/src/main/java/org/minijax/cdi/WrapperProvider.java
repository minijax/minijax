package org.minijax.cdi;

import javax.inject.Provider;

import org.minijax.MinijaxRequestContext;

class WrapperProvider<T> implements MinijaxProvider<T> {
    private final Provider<T> sourceProvider;

    public WrapperProvider(final Provider<T> sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    @Override
    public T get() {
        return sourceProvider.get();
    }

    @Override
    public T get(final MinijaxRequestContext context) {
        return sourceProvider.get();
    }
}
