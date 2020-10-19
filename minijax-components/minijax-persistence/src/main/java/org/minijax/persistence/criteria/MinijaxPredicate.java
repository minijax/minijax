package org.minijax.persistence.criteria;

import java.util.List;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public abstract class MinijaxPredicate extends MinijaxExpression<Boolean> implements jakarta.persistence.criteria.Predicate {

    public MinijaxPredicate() {
        super(Boolean.class);
    }

    @Override
    public BooleanOperator getOperator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNegated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Expression<Boolean>> getExpressions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Predicate not() {
        throw new UnsupportedOperationException();
    }
}
