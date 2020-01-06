package org.minijax.dao;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.enterprise.inject.InjectionException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;
import org.minijax.cdi.annotation.FieldAnnotationProcessor;

public class PersistenceContextAnnotationProcessor<T> implements FieldAnnotationProcessor<T> {
    private final Map<String, EntityManagerFactory> factories;

    public PersistenceContextAnnotationProcessor(final Map<String, EntityManagerFactory> factories) {
        this.factories = factories;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        if (type != EntityManager.class) {
            throw new InjectionException("Unexpected inject class for @PersistenceContext");
        }

        final PersistenceContext persistenceContext = getPersistenceContextAnnotation(annotations);
        final EntityManagerFactory emf = factories.get(persistenceContext.name());
        return (MinijaxProvider<T>) new EntityManagerProvider(emf);
    }

    private PersistenceContext getPersistenceContextAnnotation(final Annotation[] annotations) {
        for (final Annotation annotation : annotations) {
            if (annotation.annotationType() == PersistenceContext.class) {
                return (PersistenceContext) annotation;
            }
        }
        return null;
    }
}
