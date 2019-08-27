package org.minijax;

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

    @Override
    @SuppressWarnings("unchecked")
    public T getContext(final Class<?> c) {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();

        // 9.2.1
        if (c == Application.class) {
            return (T) new MinijaxApplicationView(context.getApplicationContext());
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
            return null;
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
            return (T) context.getApplicationContext().getInjector();
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
