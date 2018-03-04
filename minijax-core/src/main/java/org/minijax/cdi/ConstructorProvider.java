package org.minijax.cdi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Provider;

class ConstructorProvider<T> implements Provider<T> {
    private final Constructor<T> ctor;
    private final Provider<?>[] paramProviders;
    private final List<InjectionSet<? super T>> injectionSets;

    public ConstructorProvider(
            final Constructor<T> ctor,
            final Provider<?>[] paramProviders,
            final List<InjectionSet<? super T>> injectionSets) {

        this.ctor = ctor;
        this.paramProviders = paramProviders;
        this.injectionSets = injectionSets;
    }

    @Override
    public T get() {
        try {
            final T result = ctor.newInstance(getParams(paramProviders));
            initImpl(result);
            return result;

        } catch (final InvocationTargetException ex) {
            final Throwable inner = ex.getCause();
            throw new InjectException(inner.getMessage(), inner);

        } catch (final Exception e) {
            throw new InjectException(String.format("Can't instantiate %s", ctor), e);
        }
    }

    public T initResource(final T instance) {
        try {
            initImpl(instance);
            return instance;

        } catch (final InvocationTargetException ex) {
            final Throwable inner = ex.getCause();
            throw new InjectException(inner.getMessage(), inner);

        } catch (final Exception e) {
            throw new InjectException(String.format("Can't initialize %s", instance), e);
        }
    }

    public void initImpl(final T result)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        for (final InjectionSet<?> injectionSet : injectionSets) {
            for (final FieldProvider<?> fieldProvider : injectionSet.getFieldProviders()) {
                fieldProvider.getField().set(result, fieldProvider.getProvider().get());
            }

            for (final MethodProvider methodProvider : injectionSet.getMethodProviders()) {
                methodProvider.getMethod().invoke(result, getParams(methodProvider.getParamProviders()));
            }
        }
    }

    private Object[] getParams(final Provider<?>[] providers) {
        final Object[] params = new Object[providers.length];
        for (int i = 0; i < providers.length; ++i) {
            params[i] = providers[i].get();
        }
        return params;
    }
}
