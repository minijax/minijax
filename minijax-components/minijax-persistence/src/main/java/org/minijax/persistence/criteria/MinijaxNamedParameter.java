package org.minijax.persistence.criteria;

import java.util.Objects;

public class MinijaxNamedParameter
        extends MinijaxExpression<String>
        implements javax.persistence.criteria.ParameterExpression<String>{

    private final String name;

    public MinijaxNamedParameter(final String name) {
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
    public Class<String> getParameterType() {
        throw new UnsupportedOperationException();
    }
}
