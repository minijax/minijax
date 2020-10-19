package org.minijax.persistence.criteria;

import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import org.minijax.persistence.MinijaxSelection;
import org.minijax.persistence.criteria.MinijaxComparison.ComparisonType;

public abstract class MinijaxExpression<T>
        extends MinijaxSelection<T>
        implements jakarta.persistence.criteria.Expression<T> {

    @SuppressWarnings("unchecked")
    public static <T> MinijaxExpression<T> ofLiteral(final T value) {
        if (value == null) {
            return (MinijaxExpression<T>) MinijaxNull.INSTANCE;
        } else if (value instanceof Number) {
            return (MinijaxExpression<T>) new MinijaxNumberExpression((Number) value);
        } else if (value instanceof byte[]) {
            return (MinijaxExpression<T>) new MinijaxByteArrayExpression((byte[]) value);
        } else if (value instanceof UUID) {
            return (MinijaxExpression<T>) new MinijaxUuidExpression((UUID) value);
        } else {
            return (MinijaxExpression<T>) new MinijaxStringExpression(value.toString());
        }
    }

    protected MinijaxExpression(final Class<T> javaType) {
        super(javaType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MinijaxComparison<T> isNull() {
        return new MinijaxComparison<>(ComparisonType.IS, this, (MinijaxExpression<T>) MinijaxNull.INSTANCE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MinijaxComparison<T> isNotNull() {
        return new MinijaxComparison<>(ComparisonType.IS_NOT, this, (MinijaxExpression<T>) MinijaxNull.INSTANCE);
    }

    /*
     * Unsupported
     */

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
