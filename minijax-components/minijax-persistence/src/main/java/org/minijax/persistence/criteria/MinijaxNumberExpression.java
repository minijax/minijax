package org.minijax.persistence.criteria;

import java.util.Objects;

public class MinijaxNumberExpression extends MinijaxExpression<Number> {
    private final Number value;

    public MinijaxNumberExpression(final Number value) {
        super(Number.class);
        this.value = Objects.requireNonNull(value);
    }

    public Number getValue() {
        return value;
    }
}
