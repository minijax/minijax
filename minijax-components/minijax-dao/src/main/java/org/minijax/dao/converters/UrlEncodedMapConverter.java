package org.minijax.dao.converters;

import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.minijax.rs.util.UrlUtils;

/**
 * The UrlEncodedMapConverter class implements the JPA converter from Map to URL-encoded String.
 */
@Converter
public class UrlEncodedMapConverter implements AttributeConverter<Map<String, String>, String> {

    @Override
    public String convertToDatabaseColumn(final Map<String, String> map) {
        if (map == null) {
            return null;
        }
        return UrlUtils.urlEncodeParams(map);
    }

    @Override
    public Map<String, String> convertToEntityAttribute(final String str) {
        if (str == null) {
            return null;
        }
        return UrlUtils.urlDecodeParams(str);
    }
}
