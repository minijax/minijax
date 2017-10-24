package org.minijax.cdi;

import javax.inject.Provider;

public class ProviderProvider<T> implements Provider<T> {
    private final Provider<T> innerProvider;

    public ProviderProvider(final Provider<T> innerProvider) {
        this.innerProvider = innerProvider;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        return (T) innerProvider;
    }
}
