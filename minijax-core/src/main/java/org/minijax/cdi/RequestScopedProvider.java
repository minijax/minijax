package org.minijax.cdi;

import org.minijax.MinijaxRequestContext;

class RequestScopedProvider<T> implements MinijaxProvider<T> {
    private final Key<T> key;
    private final MinijaxProvider<T> sourceProvider;

    public RequestScopedProvider(final Key<T> key, final MinijaxProvider<T> sourceProvider) {
        this.key = key;
        this.sourceProvider = sourceProvider;
    }

    @Override
    public T get(final MinijaxRequestContext context) {
        final ResourceCache resourceCache = context.getResourceCache();

        T instance = resourceCache.get(key);

        if (instance == null) {
            instance = sourceProvider.get(context);
            resourceCache.put(key, instance);
        }

        return instance;
    }
}
