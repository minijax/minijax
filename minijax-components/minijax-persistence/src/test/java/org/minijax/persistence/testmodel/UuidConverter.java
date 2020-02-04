package org.minijax.persistence.testmodel;

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.minijax.commons.IdUtils;

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