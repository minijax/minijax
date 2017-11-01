package org.minijax;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

public class MinijaxApplicationView extends Application {
    private final Minijax container;

    public MinijaxApplicationView(final Minijax container) {
        this.container = container;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return container.getClasses();
    }

    @Override
    public Set<Object> getSingletons() {
        return container.getInjector().getSingletons();
    }

    @Override
    public Map<String, Object> getProperties() {
        return container.getConfiguration().getProperties();
    }
}
