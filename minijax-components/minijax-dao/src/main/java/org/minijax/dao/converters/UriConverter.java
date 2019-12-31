package org.minijax.dao.converters;

import java.net.URI;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UriConverter implements AttributeConverter<URI, String> {

    @Override
    public String convertToDatabaseColumn(final URI uri) {
        if (uri == null) {
            return null;
        }
        return uri.toString();
    }

    @Override
    public URI convertToEntityAttribute(final String str) {
        if (str == null) {
            return null;
        }
        return URI.create(str);
    }
}
