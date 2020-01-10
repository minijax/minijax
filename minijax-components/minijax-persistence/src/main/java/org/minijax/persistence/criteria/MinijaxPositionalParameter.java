package org.minijax.persistence.criteria;

public class MinijaxPositionalParameter
        extends MinijaxExpression<String>
        implements javax.persistence.criteria.ParameterExpression<String>{

    private final int position;

    public MinijaxPositionalParameter(final int position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getPosition() {
        return position;
    }

    @Override
    public Class<String> getParameterType() {
        throw new UnsupportedOperationException();
    }
}
