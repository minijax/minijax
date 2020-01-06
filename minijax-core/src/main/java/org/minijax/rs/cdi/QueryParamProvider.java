package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import javax.ws.rs.DefaultValue;

import org.minijax.rs.MinijaxRequestContext;

class QueryParamProvider<T> extends AbstractParamProvider<T> {

    public QueryParamProvider(final Class<T> type, final Annotation[] annotations, final String name, final DefaultValue defaultValue) {
        super(type, annotations, name, defaultValue);
    }

    @Override
    public String getStringValue(final MinijaxRequestContext ctx) {
        return ctx.getUriInfo().getQueryParameters().getFirst(name);
    }
}
