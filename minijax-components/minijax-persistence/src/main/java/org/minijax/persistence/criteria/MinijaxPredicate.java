package org.minijax.persistence.criteria;

import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public abstract class MinijaxPredicate extends MinijaxExpression<Boolean> implements javax.persistence.criteria.Predicate {

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
