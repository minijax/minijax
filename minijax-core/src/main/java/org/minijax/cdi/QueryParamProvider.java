package org.minijax.cdi;

import javax.inject.Provider;

import org.minijax.MinijaxRequestContext;

class QueryParamProvider<T> implements Provider<T> {
    private final Key<T> key;

    public QueryParamProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    public T get() {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        String value = context.getUriInfo().getQueryParameters().getFirst(key.getName());

        if (value == null && key.getDefaultValue() != null) {
            value = key.getDefaultValue().value();
        }

        return context.getApplication().convertParamToType(value, key.getType(), key.getAnnotations());
    }
}
