package org.minijax.persistence.criteria;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class MinijaxConjunction extends MinijaxPredicate {
    private final BooleanOperator operator;
    private final List<MinijaxPredicate> expressions;

    public MinijaxConjunction(final BooleanOperator operator, final Predicate... predicates) {
        this.operator = Objects.requireNonNull(operator);
        this.expressions = Arrays.stream(predicates).map(p -> (MinijaxPredicate) p).collect(toList());
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
