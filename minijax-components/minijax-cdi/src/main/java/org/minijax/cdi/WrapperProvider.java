package org.minijax.cdi;

import jakarta.inject.Provider;

public class WrapperProvider<T> implements MinijaxProvider<T> {
    private final Provider<T> sourceProvider;

    public WrapperProvider(final Provider<T> sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    @Override
    public T get() {
        return sourceProvider.get();
    }

    @Override
    public T get(final Object context) {
        return sourceProvider.get();
    }
}
