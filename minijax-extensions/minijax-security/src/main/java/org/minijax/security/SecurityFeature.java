package org.minijax.security;

import javax.inject.Provider;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.minijax.MinijaxProperties;
import org.minijax.MinijaxRequestContext;

public class SecurityFeature implements Feature {
    private final Class<?> userClass;

    public SecurityFeature(final Class<?> userClass) {
        this.userClass = userClass;
    }

    @Override
    public boolean configure(final FeatureContext context) {
        context.register(Security.class)
                .register(CsrfFilter.class)
                .register(getUserProvider(), userClass)
                .property(MinijaxProperties.SECURITY_USER_CLASS, userClass);
        return true;
    }


    public static <T extends SecurityUser> Provider<T> getUserProvider() {
        return new Provider<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T get() {
                return (T) MinijaxRequestContext.getThreadLocal().get(Security.class).getCurrentUser();
            }
        };
    }
}
