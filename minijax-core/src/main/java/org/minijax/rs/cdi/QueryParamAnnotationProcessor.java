package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;

public class QueryParamAnnotationProcessor<T> extends AbstractParamAnnotationProcessor<T> {

    @Override
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        final QueryParam queryParam = getAnnotationByType(annotations, QueryParam.class);
        final DefaultValue defaultValue = getAnnotationByType(annotations, DefaultValue.class);
        return new QueryParamProvider<>(type, annotations, queryParam.value(), defaultValue);
    }
}
