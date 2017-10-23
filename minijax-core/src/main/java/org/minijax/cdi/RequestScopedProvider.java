package org.minijax.cdi;

import javax.inject.Provider;

import org.minijax.MinijaxRequestContext;

public class RequestScopedProvider<T> implements Provider<T> {
    private final Key<T> key;
    private final Provider<T> sourceProvider;

    public RequestScopedProvider(final Key<T> key, final Provider<T> sourceProvider) {
        this.key = key;
        this.sourceProvider = sourceProvider;
    }

    @Override
    public T get() {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        final ResourceCache resourceCache = context.getResourceCache();

        T instance = resourceCache.get(key);

        if (instance == null) {
            instance = sourceProvider.get();
            resourceCache.put(key, instance);
        }

        return instance;
    }
}
