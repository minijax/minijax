package org.minijax.db;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.minijax.MinijaxApplication;

public class PersistenceFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        if (!(context instanceof MinijaxApplication)) {
            throw new IllegalArgumentException("Minijax PersistenceFeature only compatible with MinijaxApplication");
        }

        final MinijaxApplication app = (MinijaxApplication) context;
        app.getInjector().registerPersistence();
        return true;
    }
}
