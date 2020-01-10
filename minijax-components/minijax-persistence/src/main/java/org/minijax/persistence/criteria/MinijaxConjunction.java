package org.minijax.persistence.criteria;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Expression;

public class MinijaxConjunction extends MinijaxPredicate {
    private final BooleanOperator operator;
    private final List<MinijaxPredicate> expressions;

    public MinijaxConjunction(final BooleanOperator operator, final MinijaxPredicate... predicates) {
        this.operator = Objects.requireNonNull(operator);
        this.expressions = Arrays.asList(Objects.requireNonNull(predicates));
    }

    @Override
    public BooleanOperator getOperator() {
        return operator;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Expression<Boolean>> getExpressions() {
        return (List) expressions;
    }
}
