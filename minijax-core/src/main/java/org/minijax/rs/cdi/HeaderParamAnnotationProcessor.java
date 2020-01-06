package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;

public class HeaderParamAnnotationProcessor<T> extends AbstractParamAnnotationProcessor<T> {

    @Override
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        final HeaderParam headerParam = getAnnotationByType(annotations, HeaderParam.class);
        final DefaultValue defaultValue = getAnnotationByType(annotations, DefaultValue.class);
        return new HeaderParamProvider<>(type, annotations, headerParam.value(), defaultValue);
    }
}
