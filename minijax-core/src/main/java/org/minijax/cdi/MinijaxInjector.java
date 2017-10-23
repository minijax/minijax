package org.minijax.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * The MinijaxInjector class provides a Minijax-container-aware implementation of Java CDI (JSR 330).
 *
 * The implementation is heavily inspired by <a href="http://zsoltherpai.github.io/feather/">Feather</a>.
 */
public class MinijaxInjector {
    private final Map<Key<?>, Provider<?>> providers = new ConcurrentHashMap<>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void register(final Class<?> contract, final Object instance) {
        providers.put(Key.of(contract), new SingletonProvider(instance));
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

    @SuppressWarnings("unchecked")
    private <T> Provider<T> getProvider(final Key<T> key, final Set<Key<?>> chain) {
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
            p = buildConstructorProvider(key, chain);
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

    private <T> Provider<T> buildConstructorProvider(final Key<T> key, final Set<Key<?>> chain) {
        final Constructor<T> constructor = getConstructor(key);

        final Provider<?>[] paramProviders = getParamProviders(
                key,
                constructor.getParameterTypes(),
                constructor.getGenericParameterTypes(),
                constructor.getParameterAnnotations(),
                chain);

        final FieldProvider<?>[] fieldProviders = getFieldProviders(key, chain);

        return new ConstructorProvider<>(constructor, paramProviders, fieldProviders);
    }


    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> getConstructor(final Key<T> key) {
        final boolean staticClass = Modifier.isStatic(key.getType().getModifiers());
        final Class<?> parentClass = key.getType().getDeclaringClass();
        final boolean innerClass = !staticClass && parentClass != null;

        Constructor<T> inject = null;
        Constructor<T> noarg = null;

        for (final Constructor<T> c : ((Constructor<T>[]) key.getType().getDeclaredConstructors())) {
            if (c.isAnnotationPresent(Inject.class)) {
                if (inject == null) {
                    inject = c;
                } else {
                    throw new InjectException(String.format("%s has multiple @Inject constructors", key.getType()));
                }
            } else if (c.getParameterTypes().length == 0) {
                noarg = c;
            } else if (innerClass && c.getParameterTypes().length == 1 && c.getParameterTypes()[0] == parentClass) {
                noarg = c;
            }
        }

        final Constructor<T> constructor = inject != null ? inject : noarg;
        if (constructor == null) {
            throw new InjectException(String.format("%s doesn't have an @Inject or no-arg constructor, or a module provider", key.getType().getName()));
        }

        constructor.setAccessible(true);
        return constructor;
    }


    private Provider<?>[] getParamProviders(
            final Key<?> key,
            final Class<?>[] parameterClasses,
            final Type[] parameterTypes,
            final Annotation[][] annotations,
            final Set<Key<?>> chain) {

        final Provider<?>[] result = new Provider<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; ++i) {
            final Class<?> parameterClass = parameterClasses[i];
            final Annotation[] parameterAnnotations = annotations[i];
            final Class<?> providerType = Provider.class.equals(parameterClass) ?
                    (Class<?>) ((ParameterizedType) parameterTypes[i]).getActualTypeArguments()[0] :
                    null;
            if (providerType == null) {
                final Key<?> newKey = Key.of(parameterClass, parameterAnnotations);
                final Set<Key<?>> newChain = append(chain, key);
                if (newChain.contains(newKey)) {
                    throw new InjectException(String.format("Circular dependency: %s", chain(newChain, newKey)));
                }
                result[i] = getProvider(newKey, newChain);
            } else {
                final Key<?> newKey = Key.of(providerType, parameterAnnotations);
                result[i] = () -> getProvider(newKey, null);
            }
        }
        return result;
    }



    private FieldProvider<?>[] getFieldProviders(final Key<?> key, final Set<Key<?>> chain) {
        final Class<?> target = key.getType();
        final Set<Field> fields = getFields(target);
        final FieldProvider<?>[] fs = new FieldProvider<?>[fields.size()];
        int i = 0;
        for (final Field f : fields) {
            final Class<?> fieldType = f.getType();
            final Class<?> providerType = fieldType.equals(Provider.class) ?
                    (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0] :
                    fieldType;
            final Key<?> newKey = Key.of(providerType, f.getAnnotations());
            final Set<Key<?>> newChain = append(chain, key);
            if (newChain.contains(newKey)) {
                throw new InjectException(String.format("Circular dependency: %s", chain(newChain, newKey)));
            }
            fs[i++] = new FieldProvider<>(f, getProvider(newKey, newChain));
        }
        return fs;
    }


    private static Set<Field> getFields(final Class<?> type) {
        Class<?> current = type;
        final Set<Field> fields = new HashSet<>();
        while (!current.equals(Object.class)) {
            for (final Field field : current.getDeclaredFields()) {
                if (isInjectedField(field)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return fields;
    }


    private static boolean isInjectedField(final Field field) {
        for (final Annotation a : field.getDeclaredAnnotations()) {
            final Class<?> t = a.annotationType();
            if (t == javax.inject.Inject.class ||
                    t == javax.ws.rs.core.Context.class ||
                    t == javax.ws.rs.CookieParam.class ||
                    t == javax.ws.rs.FormParam.class ||
                    t == javax.ws.rs.HeaderParam.class ||
                    t == javax.ws.rs.QueryParam.class ||
                    t == javax.ws.rs.PathParam.class) {
                return true;
            }
        }
        return false;
    }

    private static Set<Key<?>> append(final Set<Key<?>> set, final Key<?> newKey) {
        if (set != null && !set.isEmpty()) {
            final Set<Key<?>> appended = new LinkedHashSet<>(set);
            appended.add(newKey);
            return appended;
        } else {
            return Collections.singleton(newKey);
        }
    }

    private static String chain(final Set<Key<?>> chain, final Key<?> lastKey) {
        final StringBuilder chainString = new StringBuilder();
        for (final Key<?> key : chain) {
            chainString.append(key.toString()).append(" -> ");
        }
        return chainString.append(lastKey.toString()).toString();
    }
}
