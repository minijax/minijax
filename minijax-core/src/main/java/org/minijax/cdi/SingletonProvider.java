package org.minijax.cdi;

import java.io.Closeable;

import javax.inject.Provider;

import org.minijax.util.CloseUtils;

class SingletonProvider<T> implements Provider<T>, Closeable {
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

    @Override
    public void close() {
        CloseUtils.closeQuietly(instance);
    }
}
