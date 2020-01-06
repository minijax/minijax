package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import javax.ws.rs.DefaultValue;

import org.minijax.rs.MinijaxRequestContext;

class PathParamProvider<T> extends AbstractParamProvider<T> {

    public PathParamProvider(final Class<T> type, final Annotation[] annotations, final String name, final DefaultValue defaultValue) {
        super(type, annotations, name, defaultValue);
    }

    @Override
    public String getStringValue(final MinijaxRequestContext context) {
        return context.getUriInfo().getPathParameters().getFirst(name);
    }
}
