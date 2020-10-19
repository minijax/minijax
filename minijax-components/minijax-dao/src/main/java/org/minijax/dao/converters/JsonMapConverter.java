package org.minijax.dao.converters;

import java.util.Map;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.minijax.json.Json;

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
        return Json.getObjectMapper().toJson(map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToEntityAttribute(final String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return Json.getObjectMapper().fromJson(str, Map.class);
    }
}
