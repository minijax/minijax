package org.minijax.cdi;

import javax.inject.Provider;

class SingletonProvider<T> implements Provider<T> {
    private final Provider<T> sourceProvider;
    private T instance;

    public SingletonProvider(final Provider<T> sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    public SingletonProvider(final T instance) {
        this.sourceProvider = null;
        this.instance = instance;
    }

    @Override
    public T get() {
        if (instance == null) {
            instance = sourceProvider.get();
        }
        return instance;
    }
}
