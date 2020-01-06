package org.minijax.cdi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.enterprise.inject.InjectionException;

public class ConstructorProvider<T> implements MinijaxProvider<T> {
    private final Constructor<T> ctor;
    private final MinijaxProvider<?>[] paramProviders;
    private final List<InjectionSet> injectionSets;

    public ConstructorProvider(
            final Constructor<T> ctor,
            final MinijaxProvider<?>[] paramProviders,
            final List<InjectionSet> injectionSets) {

        this.ctor = ctor;
        this.paramProviders = paramProviders;
        this.injectionSets = injectionSets;
    }

    @Override
    public T get(final Object context) {
        try {
            final T result = ctor.newInstance(getParams(paramProviders, context));
            initImpl(result, context);
            return result;

        } catch (final InvocationTargetException ex) {
            final Throwable inner = ex.getCause();
            throw new InjectionException(inner.getMessage(), inner);

        } catch (final Exception e) {
            throw new InjectionException(String.format("Can't instantiate %s", ctor), e);
        }
    }

    public T initResource(final T instance, final Object context) {
        try {
            initImpl(instance, context);
            return instance;

        } catch (final InvocationTargetException ex) {
            final Throwable inner = ex.getCause();
            throw new InjectionException(inner.getMessage(), inner);

        } catch (final Exception e) {
            throw new InjectionException(String.format("Can't initialize %s", instance), e);
        }
    }

    public void initImpl(final T result, final Object context)
            throws IllegalAccessException, InvocationTargetException {

        for (final InjectionSet injectionSet : injectionSets) {
            for (final FieldProvider<?> fieldProvider : injectionSet.getFieldProviders()) {
                fieldProvider.getField().set(result, fieldProvider.getProvider().get(context));
            }

            for (final MethodProvider methodProvider : injectionSet.getMethodProviders()) {
                methodProvider.getMethod().invoke(result, getParams(methodProvider.getParamProviders(), context));
            }
        }
    }

    private Object[] getParams(final MinijaxProvider<?>[] providers, final Object context) {
        final Object[] params = new Object[providers.length];
        for (int i = 0; i < providers.length; ++i) {
            params[i] = providers[i].get(context);
        }
        return params;
    }
}
