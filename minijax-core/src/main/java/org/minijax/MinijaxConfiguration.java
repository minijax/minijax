package org.minijax;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Feature;

public class MinijaxConfiguration implements javax.ws.rs.core.Configuration {
    private final Map<String, Object> properties;

    public MinijaxConfiguration() {
        properties = new HashMap<>();
    }

    @Override
    public RuntimeType getRuntimeType() {
        return RuntimeType.SERVER;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public Object getProperty(final String name) {
        return properties.get(name);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public boolean isEnabled(final Feature feature) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled(final Class<? extends Feature> featureClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered(final Object component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Class<?>, Integer> getContracts(final Class<?> componentClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Class<?>> getClasses() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Object> getInstances() {
        throw new UnsupportedOperationException();
    }
}
