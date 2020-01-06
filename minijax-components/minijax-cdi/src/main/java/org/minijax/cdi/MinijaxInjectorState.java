package org.minijax.cdi;

import java.util.Set;

public class MinijaxInjectorState {
    private final MinijaxInjector injector;
    private final Key<?> key;
    private final Set<Key<?>> chain;

    MinijaxInjectorState(final MinijaxInjector injector, final Key<?> key, final Set<Key<?>> chain) {
        this.injector = injector;
        this.key = key;
        this.chain = chain;
    }

    public MinijaxInjector getInjector() {
        return injector;
    }

    Key<?> getKey() {
        return key;
    }

    Set<Key<?>> getChain() {
        return chain;
    }
}
