package org.minijax.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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

    public MinijaxInjector register(final Object instance, final Class<?> contract) {
        providers.put(Key.of(contract), new SingletonProvider<>(instance));
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract) {
        providers.put(Key.of(contract), buildProvider(Key.of(component), null));
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract, final Class<? extends Annotation> qualifier) {
        providers.put(Key.of(contract), buildProvider(Key.of(component, qualifier), null));
        return this;
    }

    public MinijaxInjector register(final Class<?> component, final Class<?> contract, final String name) {
        providers.put(Key.of(contract), buildProvider(Key.of(component, name), null));
        return this;
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
        final List<InjectionSet<? super T>> injectionSets = getInjectionSets(key, chain);
        return new ConstructorProvider<>(constructor, paramProviders, injectionSets);
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


    private <T1, T2> List<FieldProvider<?>> getFieldProviders(final Key<T1> key, final Class<T2> target, final Set<Key<?>> chain) {
        final Set<Field> fields = getFields(target);
        final List<FieldProvider<?>> result = new ArrayList<>(fields.size());
        for (final Field field : fields) {
            result.add(new FieldProvider<>(field, getParamProvider(key, field.getType(), field.getGenericType(), field.getAnnotations(), chain)));
        }
        return result;
    }


    private static Set<Field> getFields(final Class<?> type) {
        final Set<Field> fields = new HashSet<>();
        for (final Field field : type.getDeclaredFields()) {
            if (isInjectedField(field)) {
                field.setAccessible(true);
                fields.add(field);
            }
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


    /**
     * Builds the list of injection methods for a class.
     *
     * JSR 330 compliance is extremely sensitive to the order of the results of this method.
     *
     * See here for an excellent reference implementation:
     *
     * https://github.com/rstiller/JSR-330
     * void com.github.jsr330.instance.TypeContainer.getMethodInformation()
     */
    @SuppressWarnings("unchecked")
    private <T> List<InjectionSet<? super T>> getInjectionSets(final Key<T> key, final Set<Key<?>> chain) {
        final List<Class<?>> types = getTypeList(key.getType());
        final Map<String, Method> map = new HashMap<>();
        final Map<Class<?>, List<Method>> typeMethods = new HashMap<>();
        final List<Method> toRemove = new ArrayList<>();
        Method oldMethod;

        for (final Class<?> type : types) {
            final List<Method> methods = new ArrayList<>();

            for (final Method method : type.getDeclaredMethods()) {
                final boolean candidate = method.isAnnotationPresent(Inject.class) && !Modifier.isAbstract(method.getModifiers());
                final String shortKey = method.getReturnType().toString() + " " + method.getName() + " " + Arrays.toString(method.getParameterTypes());
                final String packageKey = getPackageName(method) + " " + shortKey;

                if (map.containsKey(packageKey) && (oldMethod = map.get(packageKey)) != null) {
                    final int mod = oldMethod.getModifiers();
                    if (!(Modifier.isPrivate(mod) || Modifier.isStatic(mod) || mod == 0) || isSamePackage(oldMethod, method)) {
                        toRemove.add(map.get(packageKey));
                    }
                } else if (map.containsKey(shortKey) && (oldMethod = map.get(shortKey)) != null) {
                    final int mod = oldMethod.getModifiers();
                    if (Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
                        toRemove.add(map.get(shortKey));
                    }
                }

                if (candidate) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }

                    map.put(packageKey, method);
                    map.put(shortKey, method);
                    methods.add(method);
                }
            }

            typeMethods.put(type, methods);
        }

        final List<InjectionSet<? super T>> result = new ArrayList<>(types.size());
        for (final Class<?> type : types) {
            result.add((InjectionSet<? super T>) buildInjectionSet(key, chain, type, typeMethods, toRemove));
        }
        return result;
    }

    private <T1, T2> InjectionSet<T2> buildInjectionSet(
            final Key<T1> key,
            final Set<Key<?>> chain,
            final Class<T2> type,
            final Map<Class<?>, List<Method>> typeMethods,
            final List<Method> toRemove) {

        final List<Method> staticMethods = new ArrayList<>();
        final List<Method> methods = new ArrayList<>();

        for (final Method method : typeMethods.get(type)) {
            if (!toRemove.contains(method)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    staticMethods.add(method);
                } else {
                    methods.add(method);
                }
            }
        }

        final List<FieldProvider<?>> fieldProviders = getFieldProviders(key, type, chain);

        final List<MethodProvider> staticMethodProviders = new ArrayList<>();
        for (final Method method : staticMethods) {
            staticMethodProviders.add(new MethodProvider(method, getParamProviders(key, method, chain)));
        }

        final List<MethodProvider> methodProviders = new ArrayList<>();
        for (final Method method : methods) {
            methodProviders.add(new MethodProvider(method, getParamProviders(key, method, chain)));
        }

        return new InjectionSet<>(type, null, fieldProviders, staticMethodProviders, methodProviders);
    }

    private static List<Class<?>> getTypeList(final Class<?> type) {
        final List<Class<?>> types = new ArrayList<>();

        Class<?> current = type;
        while (current != Object.class) {
            types.add(current);
            current = current.getSuperclass();
        }

        Collections.reverse(types);
        return types;
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


    private static String getPackageName(final Method method) {
        final String name = method.getDeclaringClass().getName();
        final int index = name.lastIndexOf('.');
        return index == -1 ? "" : name.substring(0, index);
    }

    /**
     * Indicates if the two method are in the same package (not necessarily code base).
     * This is to avoid the removal of methods that are package private and not overridden by same-named methods in different-packaged subclasses.
     */
    private static boolean isSamePackage(final Method oldMethod, final Method method) {
        return oldMethod.getDeclaringClass().getPackage().equals(method.getDeclaringClass().getPackage());
    }
}
