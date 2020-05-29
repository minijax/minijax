package org.minijax.persistence.testmodel;

import java.time.Instant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class InstantIntegerConverter implements AttributeConverter<Instant, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final Instant instant) {
        if (instant == null) {
            return null;
        }
        return (int) instant.getEpochSecond();
    }

    @Override
    public Instant convertToEntityAttribute(final Integer timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp);
    }
}
