package org.minijax.persistence.criteria;

import java.util.Objects;

public class MinijaxNamedParameter<T>
        extends MinijaxExpression<T>
        implements jakarta.persistence.criteria.ParameterExpression<T> {

    private final String name;

    public MinijaxNamedParameter(final Class<T> javaType, final String name) {
        super(javaType);
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getPosition() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getParameterType() {
        return (Class<T>) getJavaType();
    }
}
