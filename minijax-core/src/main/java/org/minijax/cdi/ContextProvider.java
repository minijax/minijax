package org.minijax.cdi;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriInfo;

import org.minijax.MinijaxForm;
import org.minijax.MinijaxRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ContextProvider<T> implements Provider<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ContextProvider.class);
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

        if (c == UriInfo.class) {
            return (T) context.getUriInfo();
        }

        if (c == MinijaxForm.class) {
            return (T) context.getForm();
        }

        if (c == Form.class) {
            return (T) context.getForm().asForm();
        }

        LOG.error("Unrecognized @Context param: {}", c);
        throw new IllegalArgumentException("Unrecognized @Context parameter");
    }
}
