package org.minijax.persistence.criteria;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;

public abstract class MinijaxExpression<T> implements javax.persistence.criteria.Expression<T> {

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

//    public abstract void buildSql(StringBuilder builder, List<Object> params);

    /*
     * Unsupported
     */

    @Override
    public Selection<T> alias(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCompoundSelection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<? extends T> getJavaType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAlias() {
        throw new UnsupportedOperationException();
    }

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
