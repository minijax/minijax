package org.minijax.persistence.criteria;

public class MinijaxOrder implements jakarta.persistence.criteria.Order {
    private final MinijaxExpression<?> expression;
    private final boolean ascending;

    public MinijaxOrder(final MinijaxExpression<?> expression, final boolean ascending) {
        this.expression = expression;
        this.ascending = ascending;
    }

    @Override
    public MinijaxExpression<?> getExpression() {
        return expression;
    }

    @Override
    public boolean isAscending() {
        return ascending;
    }

    @Override
    public MinijaxOrder reverse() {
        return new MinijaxOrder(expression, !ascending);
    }
}
