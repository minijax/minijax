package org.minijax.cdi.annotation;

import java.lang.annotation.Annotation;

import javax.inject.Provider;

import org.minijax.cdi.ConstructorProviderBuilder;
import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;
import org.minijax.cdi.WrapperProvider;

public class DefaultFieldAnnotationProcessor<T> implements FieldAnnotationProcessor<T> {

    @Override
    @SuppressWarnings("unchecked")
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        final MinijaxProvider<T> p;

        if (javax.inject.Provider.class.isAssignableFrom(type)) {
            final T ctorProvider = (T) new ConstructorProviderBuilder<>(state).build().get(null);
            if (!(ctorProvider instanceof MinijaxProvider)) {
                p = new WrapperProvider<>((Provider<T>) ctorProvider);
            } else {
                p = (MinijaxProvider<T>) ctorProvider;
            }
        } else {
            p = (MinijaxProvider<T>) new ConstructorProviderBuilder<>(state).build();
        }

//        if (type.getAnnotation(Singleton.class) != null) {
//            return new SingletonProvider<T>(p);
//        }

        return p;
    }
}
