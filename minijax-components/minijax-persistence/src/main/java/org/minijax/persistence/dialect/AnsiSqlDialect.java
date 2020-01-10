package org.minijax.persistence.dialect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;

import org.minijax.commons.MinijaxException;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxNativeQuery;
import org.minijax.persistence.MinijaxQuery;
import org.minijax.persistence.criteria.MinijaxComparison;
import org.minijax.persistence.criteria.MinijaxConjunction;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.criteria.MinijaxExpression;
import org.minijax.persistence.criteria.MinijaxIn;
import org.minijax.persistence.criteria.MinijaxNamedParameter;
import org.minijax.persistence.criteria.MinijaxNull;
import org.minijax.persistence.criteria.MinijaxNumberExpression;
import org.minijax.persistence.criteria.MinijaxOrder;
import org.minijax.persistence.criteria.MinijaxPath;
import org.minijax.persistence.criteria.MinijaxPositionalParameter;
import org.minijax.persistence.criteria.MinijaxPredicate;
import org.minijax.persistence.criteria.MinijaxStringExpression;
import org.minijax.persistence.metamodel.ForeignReference;
import org.minijax.persistence.metamodel.MinijaxAttribute;
import org.minijax.persistence.metamodel.MinijaxEntityType;
import org.minijax.persistence.metamodel.MinijaxEntityType.MutableInt;
import org.minijax.persistence.metamodel.MinijaxMetamodel;
import org.minijax.persistence.metamodel.MinijaxSetAttribute;
import org.minijax.persistence.metamodel.MinijaxSingularAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnsiSqlDialect implements SqlDialect {
    private static final Logger LOG = LoggerFactory.getLogger(AnsiSqlDialect.class);

    /*
     * EntityManagerFactory
     */

    @Override
    public <T> void createTables(final MinijaxEntityManager em, final Class<T> entityClass) {
        final MinijaxMetamodel metamodel = em.getMetamodel();
        final MinijaxEntityType<T> entityType = metamodel.entity(entityClass);

        final StringBuilder b = new StringBuilder();
        b.append("CREATE TABLE IF NOT EXISTS ");
        b.append(entityType.getTableName());
        b.append(" (");

        boolean first = true;
        for (final MinijaxAttribute<? super T, ?> attr : entityType.getAttributesImpl()) {
            if (attr.isAssociation()) {
                continue;
            }

            if (!first) {
                b.append(", ");
            }

            b.append(attr.getColumn().getName());
            b.append(" ");
            b.append(attr.getColumn().getDatatype());

            if (attr.getColumn().isPrimaryKey()) {
                b.append(" PRIMARY KEY");
            }

            first = false;
        }

        b.append(")");
        executeUpdate(em, b.toString(), Collections.emptyList());

        for (final MinijaxAttribute<?, ?> attr : entityType.getAttributesImpl()) {
            final ForeignReference foreignReference = attr.getColumn().getForeignReference();
            if (foreignReference != null && foreignReference.isAssociation()) {
                final String sql =
                        "CREATE TABLE IF NOT EXISTS " +
                        foreignReference.getTableName() +
                        " (" +
                        attr.getColumn().getName() + " " + attr.getColumn().getDatatype() + ", " +
                        foreignReference.getColumnName() + " " + attr.getColumn().getDatatype() + ")";
                executeUpdate(em, sql, Collections.emptyList());
            }
        }
    }

    /*
     * EntityManager
     */

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> void persist(final MinijaxEntityManager em, final T entity) {
        final MinijaxMetamodel metamodel = em.getMetamodel();
        final MinijaxEntityType<T> entityType = metamodel.entity((Class<T>) entity.getClass());
        final LinkedHashSet<MinijaxAttribute<? super T, ?>> attrs = entityType.getAttributesImpl();
        final List<Object> params = new ArrayList<>();

        final StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(entityType.getTableName());
        sql.append(" (");

        boolean first = true;
        for (final MinijaxAttribute<? super T, ?> attr : attrs) {
            if (attr.isAssociation()) {
                continue;
            }
            if (!first) {
                sql.append(", ");
            }
            sql.append(attr.getColumn().getName());
            first = false;
        }

        sql.append(") VALUES (");

        first = true;
        for (final MinijaxAttribute<? super T, ?> attr : attrs) {
            if (attr.isAssociation()) {
                continue;
            }
            final Object value;
            switch (attr.getPersistentAttributeType()) {
            case BASIC:
                value = attr.getValue(entity);
                break;
            case ONE_TO_ONE:
            case MANY_TO_ONE:
                final Object referenceValue = attr.getValue(entity);
                if (referenceValue == null) {
                    value = null;
                } else {
                    value = ((MinijaxAttribute) attr.getReferenceId()).getValue(referenceValue);
                }
                break;
            default:
                LOG.warn("Unimplemented persistent attribute type: {}", attr.getPersistentAttributeType());
                value = null;
            }

            if (!first) {
                sql.append(',');
            }
            sql.append('?');
            params.add(value);
            first = false;
        }

        sql.append(")");
        executeUpdate(em, sql.toString(), params);
        updateAssociations(em, entity, entityType.getIdAttribute().getValue(entity), attrs);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T merge(final MinijaxEntityManager em, final T entity) {
        final MinijaxMetamodel metamodel = em.getMetamodel();
        final MinijaxEntityType<T> entityType = metamodel.entity((Class<T>) entity.getClass());
        final LinkedHashSet<MinijaxAttribute<? super T, ?>> attrs = entityType.getAttributesImpl();
        final Object entityId = entityType.getIdAttribute().getValue(entity);
        final List<Object> params = new ArrayList<>();
        final StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(entityType.getTableName());
        sql.append(" SET ");

        boolean first = true;
        for (final MinijaxAttribute<? super T, ?> attr : attrs) {
            if (attr.isAssociation()) {
                continue;
            }

            final Object value;
            switch (attr.getPersistentAttributeType()) {
            case BASIC:
                value = attr.getValue(entity);
                break;
            case ONE_TO_ONE:
            case MANY_TO_ONE:
                final Object referenceValue = attr.getValue(entity);
                if (referenceValue == null) {
                    value = null;
                } else {
                    value = ((MinijaxAttribute) attr.getReferenceId()).getValue(referenceValue);
                }
                break;
            default:
                // TODO: For OneToMany and ManyToMany attributes, we need multiple queries.
                // Delete all, and then insert values?
                // This is not a hot path, so slightly suboptimal implementation is ok.
                LOG.warn("Unimplemented persistent attribute type: {}", attr.getPersistentAttributeType());
                value = null;
            }

            if (!first) {
                sql.append(", ");
            }
            sql.append(attr.getColumn().getName());
            sql.append("=?");
            params.add(value);
            first = false;
        }

        sql.append(" WHERE ");
        sql.append(entityType.getIdAttribute().getColumn().getName());
        sql.append("=?");
        params.add(entityId);
        executeUpdate(em, sql.toString(), params);
        updateAssociations(em, entity, entityId, attrs);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void remove(final MinijaxEntityManager em, final T entity) {
        final MinijaxMetamodel metamodel = em.getMetamodel();
        final MinijaxEntityType<T> entityType = metamodel.entity((Class<T>) entity.getClass());
        final MinijaxSingularAttribute<T, ?> idAttribute = (MinijaxSingularAttribute<T, ?>) entityType.getIdAttribute();
        final StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(entityType.getTableName());
        sql.append(" WHERE ");
        sql.append(idAttribute.getColumn().getName());
        sql.append("=?");
        executeUpdate(em, sql.toString(), Collections.singletonList(idAttribute.getValue(entity)));
    }

    @Override
    public <T> T find(final MinijaxEntityManager em, final Class<T> entityClass, final Object primaryKey) {
        throw new UnsupportedOperationException();
    }

    /*
     * NativeQuery
     */

    @Override
    public <T> List<T> getResultList(final MinijaxEntityManager em, final MinijaxNativeQuery<T> query) {
        final MinijaxEntityType<T> entityType = em.getMetamodel().entity(query.getResultType());
        return getResultList(em, entityType, query.getSql(), query.getParams());
    }

    @Override
    public <T> T getSingleResult(final MinijaxEntityManager em, final MinijaxNativeQuery<T> query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> int executeUpdate(final MinijaxEntityManager em, final MinijaxNativeQuery<T> query) {
        throw new UnsupportedOperationException();
    }

    /*
     * CriteriaQuery
     */

    @Override
    public <T> List<T> getResultList(final MinijaxEntityManager em, final MinijaxQuery<T> query) {
      final MinijaxEntityType<T> entityType = em.getMetamodel().entity(query.getCriteriaQuery().getResultType());
      final Builder<T> builder = new Builder<>(em, query);
      builder.buildSelect();

      final String sql = builder.sql.toString();
      final List<Object> params = builder.outputParams;
      return getResultList(em, entityType, sql, params);
    }

    @Override
    public <T> T getSingleResult(final MinijaxEntityManager em, final MinijaxQuery<T> query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> int executeUpdate(final MinijaxEntityManager em, final MinijaxQuery<T> query) {
        throw new UnsupportedOperationException();
    }

    /*
     * Private helpers
     */

    @SuppressWarnings("unchecked")
    private <T> void updateAssociations(
            final MinijaxEntityManager em,
            final T entity,
            final Object entityId,
            final LinkedHashSet<MinijaxAttribute<? super T, ?>> attrs) {

        for (final MinijaxAttribute<? super T, ?> attr : attrs) {
            if (!attr.isAssociation()) {
                continue;
            }

            // Delete existing values
            executeUpdate(
                    em,
                    "DELETE FROM " + attr.getColumn().getForeignReference().getTableName() +
                    " WHERE " + attr.getColumn().getName() + "=?",
                    Collections.singletonList(entityId));

            // Add new values
            final MinijaxSetAttribute<?, ?> setAttr = (MinijaxSetAttribute<?, ?>) attr;
            final MinijaxEntityType<Object> elementType = (MinijaxEntityType<Object>) setAttr.getElementType();
            final Set<?> elements = (Set<?>) attr.getValue(entity);
            for (final Object element : elements) {
                executeUpdate(
                        em,
                        "INSERT INTO " + attr.getColumn().getForeignReference().getTableName() +
                        " (" + attr.getColumn().getName() + "," + attr.getColumn().getForeignReference().getColumnName() + ")" +
                        " VALUES " +
                        " (?,?)",
                        Arrays.asList(entityId, elementType.getIdAttribute().getValue(element)));
            }
        }
    }

    private <T> List<T> getResultList(final MinijaxEntityManager em, final MinijaxEntityType<T> resultType, final String sql, final List<Object> params) {
        final List<T> results = new ArrayList<>();
        LOG.debug("{}", sql);
        LOG.debug("{}", params);
        try (final PreparedStatement stmt = em.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (final ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(resultType.createInstanceFromRow(em, rs, new MutableInt(1)));
                }
            }
        } catch (final SQLException ex) {
            LOG.error("SQLException: {}", ex.getMessage(), ex);
            throw new MinijaxException(ex.getMessage(), ex);
        }

        return results;
    }

    private int executeUpdate(final MinijaxEntityManager em, final String sql, final List<Object> params) {
        LOG.debug("{}", sql);
        LOG.debug("{}", params);
        try (final PreparedStatement stmt = em.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            return stmt.executeUpdate();
        } catch (final SQLException ex) {
            LOG.error("SQLException: {}", ex.getMessage(), ex);
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }

    public static class Builder<T> {
        private final MinijaxMetamodel metamodel;
        private final MinijaxCriteriaQuery<T> criteriaQuery;
        private final Map<String, Object> namedParams;
        private final Map<Integer, Object> positionalParams;
        private final List<String> tables;
        private final List<String> columns;
        private final StringBuilder sql;
        private final List<Object> outputParams;

        public Builder(final MinijaxEntityManager em, final MinijaxQuery<T> query) {
            this.metamodel = em.getMetamodel();
            this.criteriaQuery = query.getCriteriaQuery();
            this.namedParams = query.getNamedParamters();
            this.positionalParams = query.getPositionalParams();
            this.tables = new ArrayList<>();
            this.columns = new ArrayList<>();
            this.sql = new StringBuilder();
            this.outputParams = new ArrayList<>();
        }

        public void buildSelect() {
            final MinijaxEntityType<T> entityType = metamodel.entity(criteriaQuery.getResultType());
            tables.add(entityType.getTableName() + " t0");
            buildSelectColumns(entityType, 0);

            sql.append("SELECT ");
            boolean first = true;
            for (final String column : columns) {
                if (!first) {
                    sql.append(',');
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

        private <T2> void buildSelectColumns(
                final MinijaxEntityType<T2> entityType,
                final int tableNumber) {

            for (final MinijaxAttribute<? super T2, ?> attr : entityType.getAttributesImpl()) {
                if (attr.isAssociation()) {
                    continue;
                }
                if (attr.getPersistentAttributeType() == PersistentAttributeType.ONE_TO_ONE ||
                        attr.getPersistentAttributeType() == PersistentAttributeType.MANY_TO_ONE) {
                    final Class<?> attributeType = attr.getJavaType();
                    final MinijaxEntityType<?> attributeEntityType = metamodel.entity(attributeType);
                    tables.add(
                            "LEFT JOIN " + attributeEntityType.getTableName() + " t" + (tableNumber + 1) +
                            " ON t" + tableNumber + "." + attr.getColumn().getName() + "=t" + (tableNumber + 1) + ".ID");
                    buildSelectColumns(attributeEntityType, tableNumber + 1);
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
                buildInfixOperatorSql((MinijaxComparison) expression);

            } else if (expression instanceof MinijaxIn) {
                buildInSql((MinijaxIn<T>) expression);

            } else if (expression instanceof MinijaxPath) {
                buildPathSql((MinijaxPath<T>) expression);

            } else if (expression instanceof MinijaxNamedParameter) {
                buildNamedParameterSql((MinijaxNamedParameter) expression);

            } else if (expression instanceof MinijaxPositionalParameter) {
                buildPositionalParameterSql((MinijaxPositionalParameter) expression);

            } else if (expression instanceof MinijaxNull) {
                sql.append("NULL");

            } else if (expression instanceof MinijaxNumberExpression) {
                sql.append(((MinijaxNumberExpression) expression).getValue());

            } else if (expression instanceof MinijaxStringExpression) {
                sql.append('\'').append(((MinijaxStringExpression) expression).getValue()).append('\'');
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

        private void buildInfixOperatorSql(final MinijaxComparison infixOperator) {
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

        private void buildPathSql(final MinijaxPath<T> path) {
            // TODO: Parse the value, and map to sql table alias
            sql.append(path.getValue()
                    .replace("e.", "t0.")
                    .replace("m.user", "t0.USER_ID")
                    .replace("m.", "t0."));
        }

        private void buildNamedParameterSql(final MinijaxNamedParameter variable) {
            buildVariableSql(namedParams.get(variable.getName()));
        }

        private void buildPositionalParameterSql(final MinijaxPositionalParameter variable) {
            buildVariableSql(positionalParams.get(variable.getPosition()));
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private void buildVariableSql(final Object value) {
            if (value == null) {
                sql.append("NULL");
                return;
            }

            if (value instanceof Set) {
                boolean first = true;
                for (final Object element : (Set<?>) value) {
                    if (!first) {
                        sql.append(",");
                    }
                    buildVariableSql(element);
                    first = false;
                }
                return;
            }

            sql.append('?');
            try {
                final MinijaxEntityType entityType = metamodel.entity(value.getClass());
                outputParams.add(entityType.getIdAttribute().getValue(value));
            } catch (final MinijaxException ex) {
                outputParams.add(value);
            }
        }

        private void buildOrderSql(final MinijaxOrder order) {
            buildSql(order.getExpression());

            if (!order.isAscending()) {
                sql.append(" DESC");
            }
        }
    }
}
