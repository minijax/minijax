package org.minijax.cdi;

import javax.inject.Provider;

import org.minijax.MinijaxRequestContext;

public interface MinijaxProvider<T> extends Provider<T> {

    T get(MinijaxRequestContext context);


    @Override
    default T get() {
        return get(null);
    }
}
