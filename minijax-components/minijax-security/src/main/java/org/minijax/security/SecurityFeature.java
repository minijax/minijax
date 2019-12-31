package org.minijax.security;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.minijax.MinijaxProperties;
import org.minijax.MinijaxRequestContext;
import org.minijax.cdi.MinijaxProvider;

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
                .register(new SecurityFeatureUserProvider(), userClass)
                .property(MinijaxProperties.SECURITY_USER_CLASS, userClass);
        return true;
    }

    public static class SecurityFeatureUserProvider implements MinijaxProvider<SecurityUser> {

        @Override
        @SuppressWarnings("unchecked")
        public SecurityUser get(final MinijaxRequestContext context) {
            return context.get(Security.class).getUserPrincipal();
        }

        @Override
        public SecurityUser get() {
            throw new UnsupportedOperationException();
        }
    }
}
