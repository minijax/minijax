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

import javax.inject.Inject;
import javax.inject.Provider;

import org.minijax.util.OptionalClasses;

/**
 * The ConstructorProviderBuilder class builds the list of injection methods for a class.
 *
 * JSR 330 compliance is extremely sensitive to the order of the results of this class.
 *
 * See here for an excellent reference implementation:
 *
 *     https://github.com/rstiller/JSR-330
 *     void com.github.jsr330.instance.TypeContainer.getMethodInformation()
 *
 * See here for the JSR-330 specification:
 *
 *     https://jcp.org/en/jsr/detail?id=330
 */
class ConstructorProviderBuilder<T> {
    private final MinijaxInjector injector;
    private final Key<T> key;
    private final Set<Key<?>> chain;
    private final List<Class<?>> types;
    private final Map<String, Method> map;
    private final Map<Class<?>, List<Method>> typeMethods;
    private final List<Method> toRemove;

    public ConstructorProviderBuilder(final MinijaxInjector injector, final Key<T> key, final Set<Key<?>> chain) {
        this.injector = injector;
        this.key = key;
        this.chain = chain;
        types = getTypeList(key.getType());
        map = new HashMap<>();
        typeMethods = new HashMap<>();
        toRemove = new ArrayList<>();
    }

    public Provider<T> buildConstructorProvider() {
        final Constructor<T> constructor = getConstructor(key);
        final Provider<?>[] paramProviders = getParamProviders(key, constructor, chain);
        final List<InjectionSet<? super T>> injectionSets = buildInjectionSets();
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
            } else if (innerClass && c.getParameterTypes().length == 1) {
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

    @SuppressWarnings("unchecked")
    private List<InjectionSet<? super T>> buildInjectionSets() {
        for (final Class<?> type : types) {
            processType(type);
        }

        final List<InjectionSet<? super T>> result = new ArrayList<>(types.size());
        for (final Class<?> type : types) {
            result.add((InjectionSet<? super T>) buildInjectionSet(key, chain, type, typeMethods, toRemove));
        }
        return result;
    }

    private void processType(final Class<?> type) {
        final List<Method> methods = new ArrayList<>();

        for (final Method method : type.getDeclaredMethods()) {
            if (processMethod(method)) {
                methods.add(method);
            }
        }

        typeMethods.put(type, methods);
    }

    private boolean processMethod(final Method method) {
        final boolean candidate = isCandidate(method);
        final String shortKey = buildShortKey(method);
        final String packageKey = getPackageName(method) + " " + shortKey;
        Method oldMethod;

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
            method.setAccessible(true);
            map.put(packageKey, method);
            map.put(shortKey, method);
        }

        return candidate;
    }

    private static boolean isCandidate(final Method method) {
        return method.isAnnotationPresent(Inject.class) && !Modifier.isAbstract(method.getModifiers());
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
                    t == javax.ws.rs.PathParam.class ||
                    t == javax.ws.rs.BeanParam.class ||
                    t == OptionalClasses.PERSISTENCE_CONTEXT) {
                return true;
            }
        }
        return false;
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
            return injector.getProvider(newKey, newChain);
        } else {
            final Key<?> newKey = Key.of(providerType, paramAnnotations);
            return () -> injector.getProvider(newKey, null);
        }
    }

    private static Set<Key<?>> append(final Set<Key<?>> set, final Key<?> newKey) {
        if (set != null) {
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

    private static String buildShortKey(final Method method) {
        return method.getReturnType().toString() + " " + method.getName() + " " + Arrays.toString(method.getParameterTypes());
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