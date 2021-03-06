package org.minijax.dao.converters;

import java.util.UUID;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.minijax.commons.IdUtils;

/**
 * The UuidConverter class implements the JPA converter from UUID to byte array.
 */
@Converter
public class UuidConverter implements AttributeConverter<UUID, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(final UUID uuid) {
        return IdUtils.toBytes(uuid);
    }

    @Override
    public UUID convertToEntityAttribute(final byte[] bytes) {
        return IdUtils.fromBytes(bytes);
    }
}
