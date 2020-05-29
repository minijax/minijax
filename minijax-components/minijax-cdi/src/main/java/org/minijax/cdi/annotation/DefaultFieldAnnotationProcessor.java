package org.minijax.cdi.annotation;

import java.lang.annotation.Annotation;

import jakarta.inject.Provider;

import org.minijax.cdi.ConstructorProviderBuilder;
import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;
import org.minijax.cdi.WrapperProvider;

public class DefaultFieldAnnotationProcessor<T> implements FieldAnnotationProcessor<T> {

    @Override
    @SuppressWarnings("unchecked")
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        if (jakarta.inject.Provider.class.isAssignableFrom(type)) {
            final T ctorProvider = (T) new ConstructorProviderBuilder<>(state).build().get(null);
            if (!(ctorProvider instanceof MinijaxProvider)) {
                return new WrapperProvider<>((Provider<T>) ctorProvider);
            } else {
                return (MinijaxProvider<T>) ctorProvider;
            }
        } else {
            return (MinijaxProvider<T>) new ConstructorProviderBuilder<>(state).build();
        }
    }
}
