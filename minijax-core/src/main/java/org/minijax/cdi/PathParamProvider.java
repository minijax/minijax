package org.minijax.cdi;

import org.minijax.MinijaxRequestContext;

class PathParamProvider<T> implements MinijaxProvider<T> {
    private final Key<T> key;

    public PathParamProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    public T get(final MinijaxRequestContext context) {
        return context.getApplicationContext().convertParamToType(
                context.getUriInfo().getPathParameters().getFirst(key.getName()),
                key.getType(),
                key.getAnnotations());
    }
}
