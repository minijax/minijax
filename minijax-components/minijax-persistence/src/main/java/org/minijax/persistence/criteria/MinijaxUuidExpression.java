package org.minijax.persistence.criteria;

import java.util.Objects;
import java.util.UUID;

public class MinijaxUuidExpression extends MinijaxExpression<UUID> {
    private final UUID value;

    public MinijaxUuidExpression(final UUID value) {
        super(UUID.class);
        this.value = Objects.requireNonNull(value);
    }

    public UUID getValue() {
        return value;
    }
}
