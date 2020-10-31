package org.minijax.rs.converters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;

public class ConstructorParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        try {
            return new ConstructorParamConverter<>(rawType.getDeclaredConstructor(String.class));
        } catch (final NoSuchMethodException ex) {
            // Silently ignore if no constructor that takes a single string param
            return null;
        }
    }
}
