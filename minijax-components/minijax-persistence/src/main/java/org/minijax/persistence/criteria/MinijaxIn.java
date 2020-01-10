package org.minijax.persistence.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Expression;

public class MinijaxIn<T> extends MinijaxPredicate implements javax.persistence.criteria.CriteriaBuilder.In<T> {
    private final MinijaxExpression<T> expression;
    private final List<MinijaxExpression<? extends T>> values;

    public MinijaxIn(final MinijaxExpression<T> expression) {
        this.expression = Objects.requireNonNull(expression);
        this.values = new ArrayList<>();
    }

    @Override
    public MinijaxExpression<T> getExpression() {
        return expression;
    }

    public List<MinijaxExpression<? extends T>> getValues() {
        return values;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MinijaxIn<T> value(final T value) {
//        if (value instanceof Set) {
//            final Set<? extends T> valueSet = (Set<? extends T>) value;
//            for (final Object element : valueSet) {
//                values.add((MinijaxExpression<? extends T>) MinijaxExpression.ofLiteral(element));
//            }
//        } else {
//        }
        values.add(MinijaxExpression.ofLiteral(value));
        return this;
    }

    @Override
    public MinijaxIn<T> value(final Expression<? extends T> value) {
        values.add((MinijaxExpression<? extends T>) value);
        return this;
    }
}
