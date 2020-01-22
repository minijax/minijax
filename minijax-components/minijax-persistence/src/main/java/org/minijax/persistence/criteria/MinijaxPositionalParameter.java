package org.minijax.persistence.criteria;

public class MinijaxPositionalParameter<T>
        extends MinijaxExpression<T>
        implements javax.persistence.criteria.ParameterExpression<T> {

    private final int position;

    public MinijaxPositionalParameter(final Class<T> javaType, final int position) {
        super(javaType);
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
    public Class<T> getParameterType() {
        throw new UnsupportedOperationException();
    }
}
