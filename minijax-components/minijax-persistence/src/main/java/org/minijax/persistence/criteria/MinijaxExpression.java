package org.minijax.persistence.criteria;

import java.util.Collection;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.minijax.persistence.MinijaxSelection;

public abstract class MinijaxExpression<T>
        extends MinijaxSelection<T>
        implements javax.persistence.criteria.Expression<T> {

    @SuppressWarnings("unchecked")
    public static <T> MinijaxExpression<T> ofLiteral(final T value) {
        if (value == null) {
            return (MinijaxExpression<T>) MinijaxNull.INSTANCE;
        } else if (value instanceof Number) {
            return (MinijaxExpression<T>) new MinijaxNumberExpression((Number) value);
        } else {
            return (MinijaxExpression<T>) new MinijaxStringExpression(value.toString());
        }
    }

    public MinijaxExpression(final Class<T> javaType) {
        super(javaType);
    }

    /*
     * Unsupported
     */

    @Override
    public Predicate isNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate isNotNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate in(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate in(final Expression<?>... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate in(final Collection<?> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate in(final Expression<Collection<?>> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X> Expression<X> as(final Class<X> type) {
        throw new UnsupportedOperationException();
    }
}
