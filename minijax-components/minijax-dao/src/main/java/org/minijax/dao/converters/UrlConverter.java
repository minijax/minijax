package org.minijax.dao.converters;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.minijax.commons.MinijaxException;

@Converter
public class UrlConverter implements AttributeConverter<URL, String> {

    @Override
    public String convertToDatabaseColumn(final URL url) {
        if (url == null) {
            return null;
        }
        return url.toString();
    }

    @Override
    public URL convertToEntityAttribute(final String str) {
        if (str == null) {
            return null;
        }
        try {
            return new URL(str);
        } catch (final MalformedURLException ex) {
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }
}
