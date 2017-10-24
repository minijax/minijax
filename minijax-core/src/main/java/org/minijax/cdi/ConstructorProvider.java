package org.minijax.cdi;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.inject.Provider;

public class ConstructorProvider<T> implements Provider<T> {
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

            for (final InjectionSet<?> injectionSet : injectionSets) {
                for (final FieldProvider<?> fieldProvider : injectionSet.getFieldProviders()) {
                    fieldProvider.getField().set(result, fieldProvider.getProvider().get());
                }

                for (final MethodProvider methodProvider : injectionSet.getMethodProviders()) {
                    methodProvider.getMethod().invoke(result, getParams(methodProvider.getParamProviders()));
                }
            }

            return result;

        } catch (final Exception e) {
            throw new InjectException(String.format("Can't instantiate %s", ctor), e);
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
