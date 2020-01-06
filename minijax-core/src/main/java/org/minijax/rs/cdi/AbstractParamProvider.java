package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import javax.ws.rs.DefaultValue;

import org.minijax.cdi.MinijaxProvider;
import org.minijax.rs.MinijaxRequestContext;

abstract class AbstractParamProvider<T> implements MinijaxProvider<T> {
    protected final Class<T> type;
    protected final Annotation[] annotations;
    protected final String name;
    protected final DefaultValue defaultValue;

    public AbstractParamProvider(final Class<T> type, final Annotation[] annotations, final String name, final DefaultValue defaultValue) {
        this.type = type;
        this.annotations = annotations;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    @Override
    public T get(final Object obj) {
        final MinijaxRequestContext context = (MinijaxRequestContext) obj;
        String value = getStringValue(context);

        if (value == null && defaultValue != null) {
            value = defaultValue.value();
        }

        return context.getApplicationContext().convertParamToType(value, type, annotations);
    }

    public abstract String getStringValue(MinijaxRequestContext ctx);
}
