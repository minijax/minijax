package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import jakarta.ws.rs.DefaultValue;

import org.minijax.rs.MinijaxRequestContext;

class HeaderParamProvider<T> extends AbstractParamProvider<T> {

    public HeaderParamProvider(final Class<T> type, final Annotation[] annotations, final String name, final DefaultValue defaultValue) {
        super(type, annotations, name, defaultValue);
    }

    @Override
    public String getStringValue(final MinijaxRequestContext context) {
        return context.getHeaderString(name);
    }
}
