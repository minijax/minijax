package org.minijax.persistence.criteria;

import java.util.Objects;

public class MinijaxNumberExpression extends MinijaxExpression<String> {
    private final Number value;

    public MinijaxNumberExpression(final Number value) {
        this.value = Objects.requireNonNull(value);
    }

    public Number getValue() {
        return value;
    }
}
