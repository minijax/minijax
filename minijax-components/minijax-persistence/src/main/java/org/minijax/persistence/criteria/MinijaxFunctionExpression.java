package org.minijax.persistence.criteria;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MinijaxFunctionExpression<T> extends MinijaxExpression<T> {
    private final String name;
    private final List<MinijaxExpression<T>> args;

    @SuppressWarnings("unchecked")
    public MinijaxFunctionExpression(final Class<T> resultType, final String name, final MinijaxExpression<T>... args) {
        super(resultType);
        this.name = Objects.requireNonNull(name);
        this.args = Arrays.asList(args);
    }

    public String getName() {
        return name;
    }

    public List<MinijaxExpression<T>> getArgs() {
        return args;
    }
}
