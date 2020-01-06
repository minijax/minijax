package org.minijax.rs.cdi;

import java.lang.annotation.Annotation;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;

import org.minijax.cdi.MinijaxInjectorState;
import org.minijax.cdi.MinijaxProvider;

public class CookieParamAnnotationProcessor<T> extends AbstractParamAnnotationProcessor<T> {

    @Override
    public MinijaxProvider<T> buildProvider(final MinijaxInjectorState state, final Class<T> type, final Annotation[] annotations) {
        final CookieParam cookieParam = getAnnotationByType(annotations, CookieParam.class);
        final DefaultValue defaultValue = getAnnotationByType(annotations, DefaultValue.class);
        return new CookieParamProvider<>(type, annotations, cookieParam.value(), defaultValue);
    }
}
