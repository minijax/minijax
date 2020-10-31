package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import jakarta.ws.rs.DefaultValue;

import org.minijax.cdi.MinijaxProvider;
import org.minijax.rs.MinijaxRequestContext;

/**
 * Base class for common ParamProvider classes.
 *
 * Implements common functionality for HeaderParamProvider, FormParamProvider, etc.
 */
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

        return context.convertParamToType(value, type, annotations);
    }

    public abstract String getStringValue(MinijaxRequestContext ctx);
}
