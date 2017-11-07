package org.minijax.security;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.minijax.MinijaxProperties;

public class SecurityFeature implements Feature {
    private final Class<?> userClass;

    public SecurityFeature(final Class<?> userClass) {
        this.userClass = userClass;
    }

    @Override
    public boolean configure(final FeatureContext context) {
        context.register(Security.class)
                .property(MinijaxProperties.SECURITY_USER_CLASS, userClass);
        return true;
    }
}
