package org.minijax.persistence.criteria;

public class MinijaxComparison extends MinijaxPredicate {
    public static enum ComparisonType {
        EQUALS("="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUAL_TO(">="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL_TO("<="),
        NOT_EQUALS("<>");

        private final String sql;

        private ComparisonType(final String sql) {
            this.sql = sql;
        }

        public String sql() {
            return sql;
        }
    }

    private final ComparisonType comparisonType;
    private final MinijaxExpression<?> x;
    private final MinijaxExpression<?> y;

    public MinijaxComparison(
            final ComparisonType comparisonType,
            final MinijaxExpression<?> x,
            final MinijaxExpression<?> y) {
        this.comparisonType = comparisonType;
        this.x = x;
        this.y = y;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public MinijaxExpression<?> getX() {
        return x;
    }

    public MinijaxExpression<?> getY() {
        return y;
    }
}
