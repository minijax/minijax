package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;

public class PathParamAnnotationProcessor<T> extends AbstractParamAnnotationProcessor<T> {

    @Override
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        final PathParam pathParam = getAnnotationByType(annotations, PathParam.class);
        final DefaultValue defaultValue = getAnnotationByType(annotations, DefaultValue.class);
        return new PathParamProvider<>(type, annotations, pathParam.value(), defaultValue);
    }
}
