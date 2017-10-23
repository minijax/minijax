package org.minijax.cdi;

import javax.inject.Provider;

import org.minijax.MinijaxRequestContext;

public class HeaderParamProvider<T> implements Provider<T> {
    private final Key<T> key;

    public HeaderParamProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    public T get() {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        return context.getContainer().convertStringToType(context.getHeaderString(key.getName()), key.getType());
    }
}
