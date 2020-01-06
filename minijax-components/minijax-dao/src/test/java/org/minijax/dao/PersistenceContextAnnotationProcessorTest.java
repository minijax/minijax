package org.minijax.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.InjectionException;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceProperty;
import javax.persistence.SynchronizationType;

import org.junit.Test;

public class PersistenceContextAnnotationProcessorTest {

    @Test
    public void testSimple() {
        final Map<String, EntityManagerFactory> factories = createFactories("");
        final PersistenceContextAnnotationProcessor p = new PersistenceContextAnnotationProcessor(factories);
        final PersistenceContext annotation = createAnnotation("");
        final Provider<EntityManager> provider = p.buildProvider(null, EntityManager.class, new Annotation[] { annotation });
        assertNotNull(provider);
    }

    @Test(expected = InjectionException.class)
    public void testNotFound() {
        final Map<String, EntityManagerFactory> factories = createFactories("foo", "bar");
        final PersistenceContextAnnotationProcessor p = new PersistenceContextAnnotationProcessor(factories);
        final PersistenceContext annotation = createAnnotation("baz");
        p.buildProvider(null, EntityManager.class, new Annotation[] { annotation });
    }

    @Test(expected = InjectionException.class)
    public void testMissingAnnotation() {
        final Map<String, EntityManagerFactory> factories = createFactories("foo", "bar");
        final PersistenceContextAnnotationProcessor p = new PersistenceContextAnnotationProcessor(factories);
        p.getPersistenceContextAnnotation(new Annotation[0]);
    }

    private Map<String, EntityManagerFactory> createFactories(final String... names) {
        final Map<String, EntityManagerFactory> factories = new HashMap<>();

        for (final String name : names) {
            factories.put(name, mock(EntityManagerFactory.class));
        }

        return factories;
    }

    private PersistenceContext createAnnotation(final String name) {
        return new PersistenceContext() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return PersistenceContext.class;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String unitName() {
                throw new UnsupportedOperationException();
            }

            @Override
            public PersistenceContextType type() {
                throw new UnsupportedOperationException();
            }

            @Override
            public SynchronizationType synchronization() {
                throw new UnsupportedOperationException();
            }

            @Override
            public PersistenceProperty[] properties() {
                throw new UnsupportedOperationException();
            }};
    }
}
