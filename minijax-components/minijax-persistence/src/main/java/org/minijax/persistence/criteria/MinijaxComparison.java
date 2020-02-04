package org.minijax.persistence.criteria;

public class MinijaxComparison<T> extends MinijaxPredicate {
    public enum ComparisonType {
        EQUALS("="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUAL_TO(">="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL_TO("<="),
        NOT_EQUALS("<>"),
        IS(" IS "),
        IS_NOT(" IS NOT "),
        LIKE(" LIKE ");

        private final String sql;

        private ComparisonType(final String sql) {
            this.sql = sql;
        }

        public String sql() {
            return sql;
        }
    }

    private final ComparisonType comparisonType;
    private final MinijaxExpression<T> x;
    private final MinijaxExpression<T> y;

    public MinijaxComparison(
            final ComparisonType comparisonType,
            final MinijaxExpression<T> x,
            final MinijaxExpression<T> y) {
        this.comparisonType = comparisonType;
        this.x = x;
        this.y = y;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public MinijaxExpression<T> getX() {
        return x;
    }

    public MinijaxExpression<T> getY() {
        return y;
    }
}
