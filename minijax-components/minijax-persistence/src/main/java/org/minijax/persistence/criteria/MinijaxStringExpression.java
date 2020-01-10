package org.minijax.persistence.criteria;

import java.util.Objects;

public class MinijaxStringExpression extends MinijaxExpression<String> {
    private final String value;

    public MinijaxStringExpression(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public String getValue() {
        return value;
    }
}
