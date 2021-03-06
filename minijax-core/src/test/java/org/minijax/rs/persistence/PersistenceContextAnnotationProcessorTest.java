package org.minijax.rs.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.inject.InjectionException;
import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.PersistenceProperty;
import jakarta.persistence.SynchronizationType;

import org.junit.jupiter.api.Test;

class PersistenceContextAnnotationProcessorTest {

	public @interface OtherAnnotation {
	}

    @Test
    void testSimple() {
        final Map<String, EntityManagerFactory> factories = createFactories("");
        final PersistenceContextAnnotationProcessor p = new PersistenceContextAnnotationProcessor(factories);
        final PersistenceContext annotation = createAnnotation("");
        final Provider<EntityManager> provider = p.buildProvider(null, EntityManager.class, new Annotation[] { annotation });
        assertNotNull(provider);
    }

    @Test
    void testNotFound() {
        assertThrows(InjectionException.class, () -> {
        final Map<String, EntityManagerFactory> factories = createFactories("foo", "bar");
        final PersistenceContextAnnotationProcessor p = new PersistenceContextAnnotationProcessor(factories);
        final PersistenceContext annotation = createAnnotation("baz");
        p.buildProvider(null, EntityManager.class, new Annotation[] { annotation });
    });
    }

    @Test
    void testMissingAnnotation() {
        assertThrows(InjectionException.class, () -> {
        final Map<String, EntityManagerFactory> factories = createFactories("foo", "bar");
        final PersistenceContextAnnotationProcessor p = new PersistenceContextAnnotationProcessor(factories);
        p.getPersistenceContextAnnotation(new Annotation[0]);
    });
    }

    @Test
    void testOtherAnnotations() {
        final Map<String, EntityManagerFactory> factories = createFactories("");
        final PersistenceContextAnnotationProcessor p = new PersistenceContextAnnotationProcessor(factories);
        final Provider<EntityManager> provider = p.buildProvider(null, EntityManager.class, new Annotation[] {
                new OtherAnnotation() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return OtherAnnotation.class;
                    }},
                createAnnotation(""),
            });
        assertNotNull(provider);
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
