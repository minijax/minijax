package org.minijax.persistence.criteria;

import java.util.List;
import java.util.Objects;

public class MinijaxListExpression<T> extends MinijaxExpression<List<T>> {
    private final List<T> value;

    @SuppressWarnings("unchecked")
    public MinijaxListExpression(final List<T> value) {
        super((Class<List<T>>) value.getClass());
        this.value = Objects.requireNonNull(value);
    }

    public List<T> getValue() {
        return value;
    }
}
