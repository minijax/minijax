package org.minijax.db;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.minijax.MinijaxException;
import org.minijax.json.MinijaxObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * The JsonMapConverter class implements the JPA converter from Map to JSON string.
 */
@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(final Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        try {
            return MinijaxObjectMapper.getInstance().writeValueAsString(map);
        } catch (final JsonProcessingException ex) {
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToEntityAttribute(final String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            return MinijaxObjectMapper.getInstance().readValue(str, Map.class);
        } catch (final IOException ex) {
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }
}
