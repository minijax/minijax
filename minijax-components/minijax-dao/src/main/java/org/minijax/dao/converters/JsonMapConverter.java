package org.minijax.dao.converters;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.minijax.json.Json;
import org.minijax.rs.MinijaxException;

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
            return Json.getObjectMapper().writeValueAsString(map);
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
            return Json.getObjectMapper().readValue(str, Map.class);
        } catch (final IOException ex) {
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }
}
