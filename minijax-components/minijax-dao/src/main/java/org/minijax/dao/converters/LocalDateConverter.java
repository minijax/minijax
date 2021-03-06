package org.minijax.dao.converters;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * The LocalDateConverter class implements the JPA converter from
 * Java LocalDate to Java SQL Date.
 */
@Converter
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(final LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.valueOf(date);
    }

    @Override
    public LocalDate convertToEntityAttribute(final Date date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate();
    }
}
