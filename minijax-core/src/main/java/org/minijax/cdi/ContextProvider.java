package org.minijax.cdi;

import javax.inject.Provider;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.minijax.MinijaxApplicationView;
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

        if (c == ContainerRequestContext.class) {
            return (T) context;
        }

        if (c == HttpServletRequest.class) {
            return (T) context.getServletRequest();
        }

        if (c == HttpServletResponse.class) {
            return (T) context.getServletResponse();
        }

        if (c == ServletConfig.class) {
            return null;
        }

        if (c == ServletContext.class) {
            return (T) context.getServletRequest().getServletContext();
        }

        if (c == Application.class) {
            return (T) new MinijaxApplicationView(context.getContainer());
        }

        if (c == HttpHeaders.class) {
            return null;
        }

        if (c == UriInfo.class) {
            return (T) context.getUriInfo();
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
