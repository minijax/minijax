package org.minijax.cdi;

import java.io.Closeable;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.enterprise.inject.InjectionException;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import org.minijax.cdi.annotation.DefaultFieldAnnotationProcessor;
import org.minijax.cdi.annotation.FieldAnnotationProcessor;
import org.minijax.cdi.annotation.SingletonAnnotationProcessor;
import org.minijax.cdi.annotation.TypeAnnotationProcessor;
import org.minijax.commons.CloseUtils;
import org.minijax.commons.CopyOnWriteMap;

/**
 * The MinijaxInjector class provides a Minijax-container-aware implementation of Java CDI (JSR 330).
 *
 * The implementation is heavily inspired by <a href="http://zsoltherpai.github.io/feather/">Feather</a>.
 */
public class MinijaxInjector implements Closeable {
    private final Map<Class<? extends Annotation>, TypeAnnotationProcessor<?>> typeAnnotationProcessors = new CopyOnWriteMap<>();
    private final Map<Class<? extends Annotation>, FieldAnnotationProcessor<?>> fieldAnnotationProcessors = new CopyOnWriteMap<>();
    private final Map<Key<?>, MinijaxProvider<?>> providers = new CopyOnWriteMap<>();

    public MinijaxInjector() {
        typeAnnotationProcessors.put(Singleton.class, new SingletonAnnotationProcessor<>());
        fieldAnnotationProcessors.put(Inject.class, new DefaultFieldAnnotationProcessor<>());
    }

    public void addTypeAnnotationProcessor(final Class<? extends Annotation> annotationType, final TypeAnnotationProcessor<?> processor) {
        typeAnnotationProcessors.put(annotationType, processor);
    }

    public void addFieldAnnotationProcessor(final Class<? extends Annotation> annotationType, final FieldAnnotationProcessor<?> processor) {
        fieldAnnotationProcessors.put(annotationType, processor);
    }

    public boolean isInjectedField(final Class<?> annotationType) {
        return fieldAnnotationProcessors.containsKey(annotationType);
    }

    <T> Key<T> buildKey(final Class<T> type) {
        return new Key<>(type);
    }

    <T> Key<T> buildKey(final Class<T> type, final Class<? extends Annotation> qualifier) {
        return new Key<>(type, qualifier);
    }

    <T> Key<T> buildKey(final Class<T> type, final String name) {
        return new Key<>(type, name);
    }

    <T> Key<T> buildKey(final Class<T> type, final Annotation[] annotations) {
        Annotation injectAnnotation = null;

        for (final Annotation annotation : annotations) {
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType != Inject.class && fieldAnnotationProcessors.containsKey(annotationType)) {
                if (injectAnnotation != null) {
                    throw new InjectionException("Multiple injection annotations");
                }
                injectAnnotation = annotation;
            }
        }

        return new Key<>(type, injectAnnotation, annotations);
    }

    public MinijaxInjector register(final Object instance, final Class<?> contract) {
        final MinijaxProvider<?> provider;
        if (instance instanceof MinijaxProvider) {
            provider = (MinijaxProvider<?>) instance;
        } else if (instance instanceof Provider) {
            provider = new WrapperProvider<>((Provider<?>) instance);
        } else {
            provider = new SingletonProvider<>(instance);
        }
        providers.put(buildKey(contract), provider);
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract) {
        providers.put(buildKey(contract), getProvider(component));
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract, final Class<? extends Annotation> qualifier) {
        providers.put(buildKey(contract, qualifier), getProvider(component));
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract, final String name) {
        providers.put(buildKey(contract, name), getProvider(component));
        return this;
    }

    public Set<Object> getSingletons() {
        final Set<Object> result = new HashSet<>();
        for (final MinijaxProvider<?> provider : providers.values()) {
            if (provider instanceof SingletonProvider) {
                result.add(provider.get(null));
            }
        }
        return result;
    }

    public <T> T getResource(final Class<T> c) {
        return getProvider(c).get();
    }

    public <T> T getResource(final Class<T> c, final Object context) {
        return getProvider(c).get(context);
    }

    @SuppressWarnings("unchecked")
    public <T> T initResource(final T resource, final Object context) {
        final Provider<T> provider = (Provider<T>) getProvider(resource.getClass());
        if (!(provider instanceof ConstructorProvider)) {
            throw new InjectionException("Cannot init resource class " + resource.getClass());
        }
        final ConstructorProvider<T> ctorProvider = (ConstructorProvider<T>) provider;
        ctorProvider.initResource(resource, context);
        return resource;
    }

    public <T> MinijaxProvider<T> getProvider(final Class<T> c) {
        return getProvider(buildKey(c), null, null);
    }

    public <T> MinijaxProvider<T> getProvider(final Class<T> c, final Annotation[] annotations) {
        return getProvider(buildKey(c, annotations), null, annotations);
    }

    @SuppressWarnings({ "squid:S3824", "unchecked" })
    <T> MinijaxProvider<T> getProvider(final Key<T> key, final Set<Key<?>> chain, final Annotation[] annotations) {
        MinijaxProvider<T> result = (MinijaxProvider<T>) providers.get(key);

        if (result == null) {
            result = buildProvider(key, chain, annotations);
            providers.put(key, result);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> MinijaxProvider<T> buildProvider(
            final Key<T> key,
            final Set<Key<?>> chain,
            final Annotation[] fieldAnnotations) {

        FieldAnnotationProcessor<T> processor = null;

        if (fieldAnnotations != null) {
            for (final Annotation fieldAnnotation : fieldAnnotations) {
                final FieldAnnotationProcessor<T> p = (FieldAnnotationProcessor<T>) fieldAnnotationProcessors.get(fieldAnnotation.annotationType());
                if (p != null) {
                    if (processor != null) {
                        throw new InjectionException("Conflicting field annotations");
                    }
                    processor = p;
                }
            }
        }

        if (processor == null) {
            processor = new DefaultFieldAnnotationProcessor<>();
        }

        MinijaxProvider<T> provider = processor.buildProvider(new MinijaxInjectorState(this, key, chain), key.getType(), fieldAnnotations);

        final Annotation[] typeAnnotations = key.getType().getAnnotations();

        for (final Annotation typeAnnotation : typeAnnotations) {
            final TypeAnnotationProcessor<T> p = (TypeAnnotationProcessor<T>) typeAnnotationProcessors.get(typeAnnotation.annotationType());
            if (p != null) {
                provider = p.buildProvider(provider, typeAnnotations);
            }
        }

        return provider;
    }

    @Override
    public void close() {
        CloseUtils.closeQuietly(providers.values());
    }
}
