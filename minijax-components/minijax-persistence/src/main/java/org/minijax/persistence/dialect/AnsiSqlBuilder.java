package org.minijax.persistence.dialect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate.BooleanOperator;

import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxQuery;
import org.minijax.persistence.criteria.MinijaxAttributePath;
import org.minijax.persistence.criteria.MinijaxByteArrayExpression;
import org.minijax.persistence.criteria.MinijaxComparison;
import org.minijax.persistence.criteria.MinijaxConjunction;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.criteria.MinijaxExpression;
import org.minijax.persistence.criteria.MinijaxFunctionExpression;
import org.minijax.persistence.criteria.MinijaxIn;
import org.minijax.persistence.criteria.MinijaxNamedParameter;
import org.minijax.persistence.criteria.MinijaxNull;
import org.minijax.persistence.criteria.MinijaxNumberExpression;
import org.minijax.persistence.criteria.MinijaxOrder;
import org.minijax.persistence.criteria.MinijaxPositionalParameter;
import org.minijax.persistence.criteria.MinijaxPredicate;
import org.minijax.persistence.criteria.MinijaxStringExpression;
import org.minijax.persistence.criteria.MinijaxUuidExpression;
import org.minijax.persistence.metamodel.MinijaxAttribute;
import org.minijax.persistence.metamodel.MinijaxEntityAttribute;
import org.minijax.persistence.metamodel.MinijaxEntityType;
import org.minijax.persistence.metamodel.MinijaxMetamodel;
import org.minijax.persistence.metamodel.MinijaxPluralAttribute;

public class AnsiSqlBuilder<T> {
    private final MinijaxMetamodel metamodel;
    private final MinijaxCriteriaQuery<T> criteriaQuery;
    private final Map<String, Object> namedParams;
    private final Map<Integer, Object> positionalParams;
    private final List<String> tables;
    private final List<String> columns;
    private final StringBuilder sql;
    private final List<Object> outputParams;

    public AnsiSqlBuilder(final MinijaxEntityManager em, final MinijaxQuery<T> query) {
        this.metamodel = em.getMetamodel();
        this.criteriaQuery = query.getCriteriaQuery();
        this.namedParams = query.getNamedParamters();
        this.positionalParams = query.getPositionalParams();
        this.tables = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.sql = new StringBuilder();
        this.outputParams = new ArrayList<>();
    }

    public String getSql() {
        return sql.toString();
    }

    public List<Object> getOutputParams() {
        return outputParams;
    }

    public void buildSelect() {
        final MinijaxEntityType<T> entityType = metamodel.entity(criteriaQuery.getResultType());
        tables.add(entityType.getTable().getName() + " t0");
        buildSelectColumns(entityType, 0);

        sql.append("SELECT ");
        boolean first = true;
        for (final String column : columns) {
            if (!first) {
                sql.append(", ");
            }
            sql.append(column);
            first = false;
        }

        sql.append(" FROM");
        for (final String table : tables) {
            sql.append(' ');
            sql.append(table);
        }

        final MinijaxPredicate where = criteriaQuery.getRestriction();
        if (where != null) {
            sql.append(" WHERE ");
            buildSql(where);
        }

        final List<Order> orderBy = criteriaQuery.getOrderList();
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            buildOrderSql((MinijaxOrder) orderBy.get(0));
        }
    }

    @SuppressWarnings("unchecked")
    private <T2> void buildSelectColumns(
            final MinijaxEntityType<T2> entityMapper,
            final int tableNumber) {

        for (final MinijaxAttribute<? super T2, ?> attr : entityMapper.getAttributesList()) {
            if (attr instanceof MinijaxPluralAttribute) {
                continue;
            }
            if (attr instanceof MinijaxEntityAttribute) {
                final MinijaxEntityAttribute<? super T2, ?> entityAttribute = (MinijaxEntityAttribute<? super T2, ?>) attr;
                final MinijaxEntityType<? super T2> entityType = (MinijaxEntityType<? super T2>) entityAttribute.getEntityType();
                tables.add("LEFT JOIN " + entityType.getTable().getName() + " t" + (tableNumber + 1)
                        + " ON t" + tableNumber + "." + attr.getColumn().getName() + "=t" + (tableNumber + 1)
                        + ".ID");
                buildSelectColumns(entityType, tableNumber + 1);
            } else {
                columns.add("t" + tableNumber + "." + attr.getColumn().getName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T2> void buildSql(final MinijaxExpression<T2> expression) {
        if (expression instanceof MinijaxConjunction) {
            buildConjunctionSql((MinijaxConjunction) expression);

        } else if (expression instanceof MinijaxComparison) {
            buildInfixOperatorSql((MinijaxComparison<T2>) expression);

        } else if (expression instanceof MinijaxIn) {
            buildInSql((MinijaxIn<T>) expression);

        } else if (expression instanceof MinijaxAttributePath) {
            buildAttributePathSql((MinijaxAttributePath<T>) expression);

        } else if (expression instanceof MinijaxNamedParameter) {
            buildNamedParameterSql((MinijaxNamedParameter<T2>) expression);

        } else if (expression instanceof MinijaxPositionalParameter) {
            buildPositionalParameterSql((MinijaxPositionalParameter<T2>) expression);

        } else if (expression instanceof MinijaxFunctionExpression) {
            buildFunctionExpressionSql((MinijaxFunctionExpression<T2>) expression);

        } else if (expression instanceof MinijaxNull) {
            sql.append("NULL");

        } else if (expression instanceof MinijaxNumberExpression) {
            sql.append(((MinijaxNumberExpression) expression).getValue());

        } else if (expression instanceof MinijaxStringExpression) {
            sql.append('\'').append(((MinijaxStringExpression) expression).getValue()).append('\'');

        } else if (expression instanceof MinijaxByteArrayExpression) {
            sql.append("X'").append(((MinijaxByteArrayExpression) expression).getValue()).append('\'');

        } else if (expression instanceof MinijaxUuidExpression) {
            sql.append("X'").append(((MinijaxUuidExpression) expression).getValue().toString().replace("-", "")).append('\'');

        } else {
            throw new PersistenceException("Unhandled expression type: " + expression);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void buildConjunctionSql(final MinijaxConjunction conjunction) {
        final BooleanOperator operator = conjunction.getOperator();
        final List<MinijaxExpression<?>> predicates = (List) conjunction.getExpressions();

        for (int i = 0; i < predicates.size(); i++) {
            if (i > 0) {
                sql.append(" ");
                sql.append(operator);
                sql.append(" ");
            }

            buildSql(predicates.get(i));
        }
    }

    private <T2> void buildInfixOperatorSql(final MinijaxComparison<T2> infixOperator) {
        buildSql(infixOperator.getX());
        sql.append(infixOperator.getComparisonType().sql());
        buildSql(infixOperator.getY());
    }

    private void buildInSql(final MinijaxIn<T> inPredicate) {
        buildSql(inPredicate.getExpression());
        sql.append(" IN(");

        final List<MinijaxExpression<? extends T>> values = inPredicate.getValues();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }

            buildSql((MinijaxExpression<? extends T>) values.get(i));
        }

        sql.append(")");
    }

    private void buildAttributePathSql(final MinijaxAttributePath<T> path) {
        // TODO: Parse the value, and map to sql table alias
        sql.append("t0.");
        sql.append(path.getAttribute().getColumn().getName());
    }

    private void buildNamedParameterSql(final MinijaxNamedParameter<?> variable) {
        buildVariableSql(namedParams.get(variable.getName()));
    }

    private void buildPositionalParameterSql(final MinijaxPositionalParameter<?> variable) {
        buildVariableSql(positionalParams.get(variable.getPosition()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void buildVariableSql(final Object value) {
        if (value == null) {
            sql.append("NULL");
            return;
        }

        if (value instanceof Iterable) {
            boolean first = true;
            for (final Object element : (Iterable<?>) value) {
                if (!first) {
                    sql.append(",");
                }
                buildVariableSql(element);
                first = false;
            }
            return;
        }

        sql.append('?');

        final MinijaxEntityType entityType = metamodel.entity(value.getClass());
        if (entityType != null) {
            outputParams.add(entityType.getId().getValue(value));
        } else {
            outputParams.add(value);
        }
    }

    private void buildFunctionExpressionSql(final MinijaxFunctionExpression<?> func) {
        sql.append(func.getName());
        sql.append('(');

        boolean first = true;
        for (final MinijaxExpression<?> arg : func.getArgs()) {
            if (!first) {
                sql.append(", ");
            }
            buildSql(arg);
            first = false;
        }

        sql.append(')');
    }

    private void buildOrderSql(final MinijaxOrder order) {
        buildSql(order.getExpression());

        if (!order.isAscending()) {
            sql.append(" DESC");
        }
    }
}
