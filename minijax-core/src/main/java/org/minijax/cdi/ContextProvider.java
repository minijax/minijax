package org.minijax.cdi;

import javax.inject.Provider;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.minijax.MinijaxForm;
import org.minijax.MinijaxRequestContext;

class ContextProvider<T> implements Provider<T> {
    private final Key<T> key;

    public ContextProvider(final Key<T> key) {
        this.key = key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        final MinijaxRequestContext context = MinijaxRequestContext.getThreadLocal();
        final Class<T> c = key.getType();

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
            throw new UnsupportedOperationException("Not implemented");
        }

        // 9.2.5
        if (c == SecurityContext.class) {
            return (T) context.getSecurityContext();
        }

        // 9.2.6
        if (c == Providers.class) {
            throw new UnsupportedOperationException("Not implemented");
        }

        // 9.2.7
        if (c == ResourceContext.class) {
            throw new UnsupportedOperationException("Not implemented");
        }

        // 9.2.8
        if (c == Configuration.class) {
            return (T) context.getApplication().getConfiguration();
        }

        // 10.1
        if (c == ServletConfig.class) {
            return null;
        }

        // 10.1
        if (c == ServletContext.class) {
            return (T) context.getServletRequest().getServletContext();
        }

        // 10.1
        if (c == HttpServletRequest.class) {
            return (T) context.getServletRequest();
        }

        // 10.1
        if (c == HttpServletResponse.class) {
            return (T) context.getServletResponse();
        }

        if (c == MinijaxForm.class) {
            return (T) context.getForm();
        }

        if (c == Form.class) {
            return (T) context.getForm().asForm();
        }

        throw new IllegalArgumentException("Unrecognized @Context parameter: " + c);
    }
}
