package org.minijax.rs.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;

public class ValueOfParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        try {
            return new ValueOfParamConverter<>(rawType.getDeclaredMethod("valueOf", String.class));
        } catch (final NoSuchMethodException ex) {
            // Silently ignore if no valueOf method that takes a single string param
            return null;
        }
    }
}
