package org.minijax.db.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(final List<String> list) {
        if (list == null) {
            return "";
        }
        return String.join(",", list);
    }

    @Override
    public List<String> convertToEntityAttribute(final String joined) {
        if (joined == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(joined.split(",")));
    }
}
