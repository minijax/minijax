package org.minijax.dao.converters;

import java.util.Locale;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LocaleConverter implements AttributeConverter<Locale, String> {

    @Override
    public String convertToDatabaseColumn(final Locale locale) {
        if (locale == null) {
            return null;
        }
        return locale.toLanguageTag();
    }

    @Override
    public Locale convertToEntityAttribute(final String str) {
        if (str == null) {
            return null;
        }
        return Locale.forLanguageTag(str);
    }
}
