package org.minijax;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

class MinijaxApplicationView extends Application {
    private final MinijaxApplicationContext applicationContext;

    public MinijaxApplicationView(MinijaxApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return applicationContext.getClasses();
    }

    @Override
    public Set<Object> getSingletons() {
        return applicationContext.getInstances();
    }

    @Override
    public Map<String, Object> getProperties() {
        return applicationContext.getProperties();
    }
}
