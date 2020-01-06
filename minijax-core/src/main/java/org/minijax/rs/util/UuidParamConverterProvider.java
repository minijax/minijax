package org.minijax.rs.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.UUID;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

import org.minijax.commons.IdUtils;

public class UuidParamConverterProvider implements ParamConverterProvider {
    private static UuidParamConverter converter;

    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        return rawType == UUID.class ? ((ParamConverter<T>) getConverter()) : null;
    }

    private static UuidParamConverter getConverter() {
        if (converter == null) {
            converter = new UuidParamConverter();
        }
        return converter;
    }

    static class UuidParamConverter implements ParamConverter<UUID> {

        @Override
        public UUID fromString(final String value) {
            return IdUtils.tryParse(value);
        }

        @Override
        public String toString(final UUID value) {
            return value == null ? null : value.toString();
        }
    }
}
