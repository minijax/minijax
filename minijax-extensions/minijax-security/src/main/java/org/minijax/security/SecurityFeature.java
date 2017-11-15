package org.minijax.security;

import javax.inject.Provider;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.minijax.MinijaxProperties;
import org.minijax.MinijaxRequestContext;

public class SecurityFeature implements Feature {
    private final Class<? extends SecurityUser> userClass;
    private final Class<? extends SecurityDao> daoClass;

    public SecurityFeature(
            final Class<? extends SecurityUser> userClass,
            final Class<? extends SecurityDao> daoClass) {
        this.userClass = userClass;
        this.daoClass = daoClass;
    }

    @Override
    public boolean configure(final FeatureContext context) {
        context.register(Security.class)
                .register(CsrfFilter.class)
                .register(daoClass, SecurityDao.class)
                .register(getUserProvider(), userClass)
                .property(MinijaxProperties.SECURITY_USER_CLASS, userClass);
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T extends SecurityUser> Provider<T> getUserProvider() {
        return () -> (T) MinijaxRequestContext.getThreadLocal().get(Security.class).getCurrentUser();
    }
}
