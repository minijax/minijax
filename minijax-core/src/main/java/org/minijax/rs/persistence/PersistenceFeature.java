package org.minijax.rs.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

import org.minijax.rs.MinijaxApplicationContext;

public class PersistenceFeature implements Feature {

    @Override
    public boolean configure(final FeatureContext context) {
        if (!(context instanceof MinijaxApplicationContext)) {
            throw new IllegalArgumentException("Minijax PersistenceFeature only compatible with MinijaxApplication");
        }

        final MinijaxApplicationContext app = (MinijaxApplicationContext) context;
        registerPersistence(app);
        return true;
    }

    public void registerPersistence(final MinijaxApplicationContext app) {
        final List<String> names = PersistenceUtils.getNames("META-INF/persistence.xml");
        final Map<String, Object> props = new HashMap<>();
        props.put("eclipselink.classloader", this.getClass().getClassLoader());
        props.putAll(app.getProperties());

        final Map<String, EntityManagerFactory> factories = new HashMap<>();
        boolean first = true;

        for (final String name : names) {
            final EntityManagerFactory emf = Persistence.createEntityManagerFactory(name, props);
            factories.put(name, emf);
            if (first) {
                factories.put("", emf);
                first = false;
            }
        }

        app.getInjector().addFieldAnnotationProcessor(
                PersistenceContext.class,
                new PersistenceContextAnnotationProcessor(factories));
    }
}
