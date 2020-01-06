package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;
import org.minijax.cdi.annotation.FieldAnnotationProcessor;

/**
 * The ContextAnnotationProcessor is the CDI annotation processor for the @Context annotation.
 */
public class ContextAnnotationProcessor<T> implements FieldAnnotationProcessor<T> {

    @Override
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        return new ContextProvider<>(type);
    }
}
