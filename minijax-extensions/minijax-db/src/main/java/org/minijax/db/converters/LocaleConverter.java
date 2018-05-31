package org.minijax.db.converters;

import java.util.Locale;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
