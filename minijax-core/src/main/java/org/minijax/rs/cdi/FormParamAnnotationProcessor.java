package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;

public class FormParamAnnotationProcessor<T> extends AbstractParamAnnotationProcessor<T> {

    @Override
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        final FormParam formParam = getAnnotationByType(annotations, FormParam.class);
        final DefaultValue defaultValue = getAnnotationByType(annotations, DefaultValue.class);
        return new FormParamProvider<>(type, annotations, formParam.value(), defaultValue);
    }
}
