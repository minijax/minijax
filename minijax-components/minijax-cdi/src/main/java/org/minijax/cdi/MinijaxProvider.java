package org.minijax.cdi;

import jakarta.inject.Provider;

public interface MinijaxProvider<T> extends Provider<T> {

    T get(Object context);

    @Override
    default T get() {
        return get(null);
    }
}
