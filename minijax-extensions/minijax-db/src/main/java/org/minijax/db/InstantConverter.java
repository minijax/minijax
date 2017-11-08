package org.minijax.db;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * The InstantConverter class implements the JPA converter from
 * Java Instant to Java SQL Timestamp.
 */
@Converter
public class InstantConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(final Instant instant) {
        if (instant == null) {
            return null;
        }
        return Timestamp.from(instant);
    }


    @Override
    public Instant convertToEntityAttribute(final Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toInstant();
    }
}
