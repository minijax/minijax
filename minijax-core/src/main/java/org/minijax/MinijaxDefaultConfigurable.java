package org.minijax;

import java.util.Map;

import javax.ws.rs.core.Configurable;
import javax.ws.rs.core.Configuration;

public abstract class MinijaxDefaultConfigurable<C extends Configurable<C>> implements Configurable<C> {

    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C property(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C register(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C register(final Class<?> componentClass, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C register(final Class<?> componentClass, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C register(final Class<?> componentClass, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C register(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C register(final Object component, final int priority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C register(final Object component, final Class<?>... contracts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C register(final Object component, final Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException();
    }
}
