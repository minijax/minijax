package org.minijax.cdi;

import java.io.Closeable;

import org.minijax.MinijaxRequestContext;
import org.minijax.util.CloseUtils;

class SingletonProvider<T> implements MinijaxProvider<T>, Closeable {
    private final MinijaxProvider<T> sourceProvider;
    private T instance;

    public SingletonProvider(final MinijaxProvider<T> sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    public SingletonProvider(final T instance) {
        this.sourceProvider = null;
        this.instance = instance;
    }

    @Override
    public T get(final MinijaxRequestContext context) {
        if (instance == null) {
            instance = sourceProvider.get(context);
        }
        return instance;
    }

    @Override
    public void close() {
        CloseUtils.closeQuietly(instance);
    }
}
