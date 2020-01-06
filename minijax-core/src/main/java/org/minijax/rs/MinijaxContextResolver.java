package org.minijax.rs;

import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;

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
            return (T) context.getApplicationContext().getApplication();
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
            return (T) context.getApplicationContext().getProviders();
        }

        // 9.2.7
        if (c == ResourceContext.class) {
            return (T) context;
        }

        // 9.2.8
        if (c == Configuration.class) {
            return (T) context.getApplicationContext().getConfiguration();
        }

        // 10.1
        if (c.getName().startsWith("javax.servlet.")) {
            return null;
        }

        throw new IllegalArgumentException("Unrecognized @Context parameter: " + c);
    }
}
