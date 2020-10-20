package org.minijax.rs.persistence;

import java.lang.annotation.Annotation;
import java.util.Map;

import jakarta.enterprise.inject.InjectionException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;
import org.minijax.cdi.annotation.FieldAnnotationProcessor;

public class PersistenceContextAnnotationProcessor implements FieldAnnotationProcessor<EntityManager> {
    private final Map<String, EntityManagerFactory> factories;

    public PersistenceContextAnnotationProcessor(final Map<String, EntityManagerFactory> factories) {
        this.factories = factories;
    }

    @Override
    public MinijaxProvider<EntityManager> buildProvider(
            final MinijaxInjectorState state,
            final Class<EntityManager> type,
            final Annotation[] annotations) {

        final PersistenceContext persistenceContext = getPersistenceContextAnnotation(annotations);
        final EntityManagerFactory emf = factories.get(persistenceContext.name());
        if (emf == null) {
            throw new InjectionException("Persistence context \"" + persistenceContext.name() + "\" not found");
        }

        return new EntityManagerProvider(emf);
    }

    PersistenceContext getPersistenceContextAnnotation(final Annotation[] annotations) {
        for (final Annotation annotation : annotations) {
            if (annotation.annotationType() == PersistenceContext.class) {
                return (PersistenceContext) annotation;
            }
        }
        throw new InjectionException("Missing @PersistenceContext annotation");
    }
}
