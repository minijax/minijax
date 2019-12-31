package org.minijax.dao;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.minijax.MinijaxApplicationContext;

public class PersistenceFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        if (!(context instanceof MinijaxApplicationContext)) {
            throw new IllegalArgumentException("Minijax PersistenceFeature only compatible with MinijaxApplication");
        }

        final MinijaxApplicationContext app = (MinijaxApplicationContext) context;
        app.getInjector().registerPersistence();
        return true;
    }
}
