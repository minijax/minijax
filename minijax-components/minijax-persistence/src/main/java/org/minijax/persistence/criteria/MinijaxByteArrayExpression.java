package org.minijax.persistence.criteria;

import java.util.Objects;

public class MinijaxByteArrayExpression extends MinijaxExpression<byte[]> {
    private final byte[] value;

    public MinijaxByteArrayExpression(final byte[] value) {
        super(byte[].class);
        this.value = Objects.requireNonNull(value);
    }

    public byte[] getValue() {
        return value;
    }
}
