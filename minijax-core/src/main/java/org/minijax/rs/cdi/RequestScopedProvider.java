package org.minijax.rs.cdi;

import org.minijax.cdi.MinijaxProvider;
import org.minijax.rs.MinijaxRequestContext;

class RequestScopedProvider<T> implements MinijaxProvider<T> {
    private final MinijaxProvider<T> sourceProvider;
    private final Object key;

    public RequestScopedProvider(final MinijaxProvider<T> sourceProvider) {
        this.sourceProvider = sourceProvider;
        this.key = new Object();
    }

    @Override
    public T get(final Object obj) {
        final MinijaxRequestContext context = (MinijaxRequestContext) obj;
        final ResourceCache resourceCache = context.getResourceCache();

        T instance = resourceCache.get(key);

        if (instance == null) {
            instance = sourceProvider.get(context);
            resourceCache.put(key, instance);
        }

        return instance;
    }
}
