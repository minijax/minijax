package org.minijax.cdi;

import java.io.Closeable;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.minijax.Minijax;
import org.minijax.util.CloseUtils;
import org.minijax.util.PersistenceUtils;

/**
 * The MinijaxInjector class provides a Minijax-container-aware implementation of Java CDI (JSR 330).
 *
 * The implementation is heavily inspired by <a href="http://zsoltherpai.github.io/feather/">Feather</a>.
 */
public class MinijaxInjector implements Closeable {
    private final Minijax container;
    private final Map<Key<?>, Provider<?>> providers = new ConcurrentHashMap<>();

    public MinijaxInjector() {
        this(null);
    }

    public MinijaxInjector(final Minijax container) {
        this.container = container;
    }

    public MinijaxInjector register(final Object instance, final Class<?> contract) {
        final Provider<?> provider = instance instanceof Provider
                ? (Provider<?>) instance
                : new SingletonProvider<>(instance);
        providers.put(Key.of(contract), provider);
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract) {
        providers.put(Key.of(contract), buildProvider(Key.of(component), null));
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract, final Class<? extends Annotation> qualifier) {
        providers.put(Key.of(contract, qualifier), buildProvider(Key.of(component), null));
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract, final String name) {
        providers.put(Key.of(contract, name), buildProvider(Key.of(component), null));
        return this;
    }

    public Set<Object> getSingletons() {
        final Set<Object> result = new HashSet<>();
        for (final Provider<?> provider : providers.values()) {
            if (provider instanceof SingletonProvider) {
                result.add(provider.get());
            }
        }
        return result;
    }

    public <T> T get(final Class<T> c) {
        return getProvider(c).get();
    }

    public <T> T get(final Class<T> c, final Annotation[] annotations) {
        return getProvider(c, annotations).get();
    }

    public <T> Provider<T> getProvider(final Class<T> c) {
        return getProvider(Key.<T>of(c), null);
    }

    public <T> Provider<T> getProvider(final Class<T> c, final Annotation[] annotations) {
        return getProvider(Key.<T>of(c, annotations), null);
    }

    @SuppressWarnings({ "squid:S3824", "unchecked" })
    <T> Provider<T> getProvider(final Key<T> key, final Set<Key<?>> chain) {
        Provider<T> result = (Provider<T>) providers.get(key);

        if (result == null) {
            result = buildProvider(key, chain);
            providers.put(key, result);
        }

        return result;
    }


    private <T> Provider<T> buildProvider(final Key<T> key, final Set<Key<?>> chain) {
        final Provider<T> p;

        switch (key.getStrategy()) {
        case CONTEXT:
            p = new ContextProvider<>(key);
            break;
        case COOKIE:
            p = new CookieParamProvider<>(key);
            break;
        case FORM:
            p = new FormParamProvider<>(key);
            break;
        case HEADER:
            p = new HeaderParamProvider<>(key);
            break;
        case PATH:
            p = new PathParamProvider<>(key);
            break;
        case QUERY:
            p = new QueryParamProvider<>(key);
            break;
        default:
            p = new ConstructorProviderBuilder<>(this, key, chain).buildConstructorProvider();
            break;
        }

        if (key.getType().getAnnotation(Singleton.class) != null) {
            return new SingletonProvider<>(p);
        } else if (key.getType().getAnnotation(RequestScoped.class) != null) {
            return new RequestScopedProvider<>(key, p);
        } else {
            return p;
        }
    }


    /*
     * Persistence
     */


    public void registerPersistence() {
        final List<String> names = PersistenceUtils.getNames("META-INF/persistence.xml");
        final Map<String, Object> props = container == null ? null : container.getProperties();
        boolean first = true;

        for (final String name : names) {
            final EntityManagerFactory emf = Persistence.createEntityManagerFactory(name, props);
            registerEntityManagerFactory(emf, name);
            if (first) {
                registerEntityManagerFactory(emf, "");
                first = false;
            }
            initEntityManager(emf);
        }
    }


    private void registerEntityManagerFactory(final EntityManagerFactory emf, final String name) {
        final Key<EntityManagerFactory> emfKey = Key.of(EntityManagerFactory.class, name);
        final Provider<EntityManagerFactory> emfProvider = new SingletonProvider<>(emf);
        providers.put(emfKey, emfProvider);

        final Key<EntityManager> emKey = Key.ofPersistenceContext(name);
        final Provider<EntityManager> emProvider = new RequestScopedProvider<>(emKey, new EntityManagerProvider(emf));
        providers.put(emKey, emProvider);
    }


    private void initEntityManager(final EntityManagerFactory emf) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getMetamodel();
        } finally {
            CloseUtils.closeQuietly(em);
        }
    }


    @Override
    public void close() {
        CloseUtils.closeQuietly(providers.values());
    }
}
