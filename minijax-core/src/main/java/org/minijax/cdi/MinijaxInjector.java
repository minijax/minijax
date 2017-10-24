package org.minijax.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
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

    public void register(final Class<?> c, final Class<?> contract) {
        providers.put(Key.of(contract), buildProvider(Key.of(c), null));
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
        final Provider<?>[] paramProviders = getParamProviders(key, constructor, chain);
        final FieldProvider<?>[] fieldProviders = getFieldProviders(key, chain);
        final MethodProvider[] methodProviders = getMethodProviders(key, chain);
        return new ConstructorProvider<>(constructor, paramProviders, fieldProviders, methodProviders);
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


    private Provider<?>[] getParamProviders(final Key<?> key, final Executable executable, final Set<Key<?>> chain) {
        final Class<?>[] paramClasses = executable.getParameterTypes();
        final Type[] paramTypes = executable.getGenericParameterTypes();
        final Annotation[][] annotations = executable.getParameterAnnotations();
        final Provider<?>[] result = new Provider<?>[paramTypes.length];
        for (int i = 0; i < paramTypes.length; ++i) {
            result[i] = getParamProvider(key, paramClasses[i], paramTypes[i], annotations[i], chain);
        }
        return result;
    }


    private Provider<?> getParamProvider(
            final Key<?> key,
            final Class<?> parameterClass,
            final Type paramType,
            final Annotation[] paramAnnotations,
            final Set<Key<?>> chain) {

        final Class<?> providerType = Provider.class.equals(parameterClass)
                ? (Class<?>) ((ParameterizedType) paramType).getActualTypeArguments()[0]
                : null;

        if (providerType == null) {
            final Key<?> newKey = Key.of(parameterClass, paramAnnotations);
            final Set<Key<?>> newChain = append(chain, key);
            if (newChain.contains(newKey)) {
                throw new InjectException(String.format("Circular dependency: %s", chain(newChain, newKey)));
            }
            return getProvider(newKey, newChain);
        } else {
            final Key<?> newKey = Key.of(providerType, paramAnnotations);
            return () -> getProvider(newKey, null);
        }
    }


    private FieldProvider<?>[] getFieldProviders(final Key<?> key, final Set<Key<?>> chain) {
        final Class<?> target = key.getType();
        final Set<Field> fields = getFields(target);
        final FieldProvider<?>[] result = new FieldProvider<?>[fields.size()];
        int i = 0;
        for (final Field field : fields) {
            result[i++] = new FieldProvider<>(field, getParamProvider(key, field.getType(), field.getGenericType(), field.getAnnotations(), chain));
        }
        return result;
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


    private MethodProvider[] getMethodProviders(final Key<?> key, final Set<Key<?>> chain) {
        final Class<?> target = key.getType();
        final Set<Method> methods = getMethods(target);
        final MethodProvider[] result = new MethodProvider[methods.size()];
        int i = 0;
        for (final Method method : methods) {
            result[i++] = new MethodProvider(method, getParamProviders(key, method, chain));
        }
        return result;
    }


    private static Set<Method> getMethods(final Class<?> type) {
        Class<?> current = type;
        final Set<Method> methods = new HashSet<>();
        while (!current.equals(Object.class)) {
            for (final Method method : current.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Inject.class) && !isMethodOverridden(methods, method)) {
                    method.setAccessible(true);
                    methods.add(method);
                }
            }
            current = current.getSuperclass();
        }
        return methods;
    }


    private static boolean isMethodOverridden(final Set<Method> existingMethods, final Method candidateMethod) {
        for (final Method existingMethod : existingMethods) {
            if (existingMethod.getName().equals(candidateMethod.getName()) &&
                    Arrays.equals(existingMethod.getParameterTypes(), candidateMethod.getParameterTypes())) {
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
            chainString.append(key.toString()).append(" -> \n");
        }
        return chainString.append(lastKey.toString()).toString();
    }
}
