package org.minijax.cdi;

import java.lang.reflect.Method;

import javax.inject.Provider;

public class MethodProvider {
    private final Method method;
    private final Provider<?>[] paramProviders;

    public MethodProvider(final Method method, final Provider<?>[] paramProviders) {
        this.method = method;
        this.paramProviders = paramProviders;
    }

    public Method getMethod() {
        return method;
    }

    public Provider<?>[] getParamProviders() {
        return paramProviders;
    }
}
