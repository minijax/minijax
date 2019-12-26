package org.minijax.cdi;

import java.lang.reflect.Method;

class MethodProvider {
    private final Method method;
    private final MinijaxProvider<?>[] paramProviders;

    public MethodProvider(final Method method, final MinijaxProvider<?>[] paramProviders) {
        this.method = method;
        this.paramProviders = paramProviders;
    }

    public Method getMethod() {
        return method;
    }

    @SuppressWarnings("squid:S1452")
    public MinijaxProvider<?>[] getParamProviders() {
        return paramProviders;
    }
}
