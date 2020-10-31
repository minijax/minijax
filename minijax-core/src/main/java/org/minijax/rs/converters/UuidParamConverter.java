package org.minijax.rs.converters;

import java.util.UUID;

import jakarta.ws.rs.ext.ParamConverter;

import org.minijax.commons.IdUtils;

public class UuidParamConverter implements ParamConverter<UUID> {

    @Override
    public UUID fromString(final String value) {
        return IdUtils.tryParse(value);
    }

    @Override
    public String toString(final UUID value) {
        return value == null ? null : value.toString();
    }
}
