package org.minijax.rs;

import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Providers;

public class MinijaxContextResolver<T> implements ContextResolver<T> {
    private final MinijaxRequestContext context;

    public MinijaxContextResolver(final MinijaxRequestContext context) {
        this.context = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getContext(final Class<?> c) {
        return (T) MinijaxContextResolver.getContext(context, c);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getContext(final MinijaxRequestContext context, final Class<T> c) {
        // 9.2.1
        if (c == Application.class) {
            return (T) context.getApplication();
        }

        // 9.2.2
        if (c == UriInfo.class) {
            return (T) context.getUriInfo();
        }

        // 9.2.3
        if (c == HttpHeaders.class) {
            return (T) context.getHttpHeaders();
        }

        // 9.2.4
        if (c == Request.class) {
            return (T) context.getRequest();
        }

        // 9.2.5
        if (c == SecurityContext.class) {
            return (T) context.getSecurityContext();
        }

        // 9.2.6
        if (c == Providers.class) {
            return (T) context.getProviders();
        }

        // 9.2.7
        if (c == ResourceContext.class) {
            return (T) context;
        }

        // 9.2.8
        if (c == Configuration.class) {
            return (T) context.getApplication().getConfiguration();
        }

        // 10.1
        if (c.getName().startsWith("jakarta.servlet.")) {
            return null;
        }

        throw new IllegalArgumentException("Unrecognized @Context parameter: " + c);
    }
}
