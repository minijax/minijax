package org.minijax.persistence.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.PersistenceException;

import org.minijax.commons.IdUtils;
import org.minijax.persistence.MinijaxEntityManager;
import org.minijax.persistence.MinijaxNativeQuery;
import org.minijax.persistence.MinijaxQuery;
import org.minijax.persistence.metamodel.MinijaxAttribute;
import org.minijax.persistence.metamodel.MinijaxEntityType;
import org.minijax.persistence.metamodel.MinijaxMetamodel;
import org.minijax.persistence.metamodel.MinijaxPluralAttribute;
import org.minijax.persistence.metamodel.MinijaxSingularAttribute;
import org.minijax.persistence.metamodel.MutableInt;
import org.minijax.persistence.schema.Column;
import org.minijax.persistence.schema.Table;
import org.minijax.persistence.wrapper.MemberWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnsiSqlDialect implements SqlDialect {
    private static final Logger LOG = LoggerFactory.getLogger(AnsiSqlDialect.class);

    /*
     * EntityManagerFactory
     */

    @Override
    public <T> void createTables(final Connection conn, final MinijaxEntityType<T> entityType) {
        createTable(conn, entityType.getTable());

        for (final Table joinTable : entityType.getJoinTables()) {
            createTable(conn, joinTable);
        }
    }

    private void createTable(final Connection conn, final Table table) {
        final StringBuilder b = new StringBuilder();
        b.append("CREATE TABLE IF NOT EXISTS ");
        b.append(table.getName());
        b.append(" (");

        boolean first = true;
        for (final Column column : table.getColumns()) {
            if (!first) {
                b.append(", ");
            }

            b.append(column.getName());
            b.append(" ");
            b.append(column.getDatatype());

            if (column.isPrimaryKey()) {
                b.append(" PRIMARY KEY");
            }

            first = false;
        }

        b.append(")");
        executeUpdate(conn, b.toString(), Collections.emptyList());
    }

    /*
     * EntityManager
     */

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> void persist(final MinijaxEntityManager em, final T entity) {
        final MinijaxMetamodel metamodel = em.getMetamodel();
        final MinijaxEntityType<T> entityType = metamodel.entity((Class<T>) entity.getClass());
        final List<MinijaxAttribute<? super T, ?>> attrs = entityType.getAttributesList();
        final List<Object> params = new ArrayList<>();

        final StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(entityType.getTable().getName());
        sql.append(" (");

        boolean first = true;
        for (final MinijaxAttribute<? super T, ?> attr : attrs) {
            if (attr instanceof MinijaxPluralAttribute) {
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
            if (attr instanceof MinijaxPluralAttribute) {
                continue;
            }
            if (!first) {
                sql.append(',');
            }
            sql.append('?');
            params.add(attr.write(em, entity));
            first = false;
        }

        sql.append(")");
        executeUpdate(em.getConnection(), sql.toString(), params);
        updateAssociations(em, entity, entityType.getId().getValue(entity), attrs);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T merge(final MinijaxEntityManager em, final T entity) {
        final MinijaxMetamodel metamodel = em.getMetamodel();
        final MinijaxEntityType<T> entityType = metamodel.entity((Class<T>) entity.getClass());
        final List<MinijaxAttribute<? super T, ?>> attrs = entityType.getAttributesList();
        final Object entityId = entityType.getId().getValue(entity);
        final List<Object> params = new ArrayList<>();
        final StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(entityType.getTable().getName());
        sql.append(" SET ");

        boolean first = true;
        for (final MinijaxAttribute<? super T, ?> attr : attrs) {
            if (attr instanceof MinijaxPluralAttribute) {
                continue;
            }
            if (!first) {
                sql.append(", ");
            }
            sql.append(attr.getColumn().getName());
            sql.append("=?");
            params.add(attr.write(em, entity));
            first = false;
        }

        sql.append(" WHERE ");
        sql.append(entityType.getId().getColumn().getName());
        sql.append("=?");
        params.add(entityId);
        executeUpdate(em.getConnection(), sql.toString(), params);
        updateAssociations(em, entity, entityId, attrs);
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void remove(final MinijaxEntityManager em, final T entity) {
        final MinijaxMetamodel metamodel = em.getMetamodel();
        final MinijaxEntityType<T> entityType = metamodel.entity((Class<T>) entity.getClass());
        final MinijaxSingularAttribute<T, ?> idAttribute = (MinijaxSingularAttribute<T, ?>) entityType.getId();
        final StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(entityType.getTable().getName());
        sql.append(" WHERE ");
        sql.append(idAttribute.getColumn().getName());
        sql.append("=?");
        executeUpdate(
                em.getConnection(),
                sql.toString(),
                Collections.singletonList(idAttribute.getValue(entity)));
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
      final AnsiSqlBuilder<T> builder = new AnsiSqlBuilder<>(em, query);
      builder.buildSelect();
      return getResultList(em, entityType, builder.getSql(), builder.getOutputParams());
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> void updateAssociations(
            final MinijaxEntityManager em,
            final T entity,
            final Object entityId,
            final List<MinijaxAttribute<? super T, ?>> attrs) {

        for (final MinijaxAttribute<? super T, ?> attr : attrs) {
            if (!(attr instanceof MinijaxPluralAttribute)) {
                continue;
            }

            final MinijaxPluralAttribute<? super T, ?, ?> joinTableMapper = (MinijaxPluralAttribute<? super T, ?, ?>) attr;
            final Table joinTable = joinTableMapper.getColumn().getJoinTable();
            final MemberWrapper valueIdWrapper = joinTableMapper.getElementIdWrapper();

            // Delete existing values
            executeUpdate(
                    em.getConnection(),
                    "DELETE FROM " + joinTable.getName() +
                    " WHERE " + attr.getColumn().getName() + "=?",
                    Collections.singletonList(entityId));

            final Iterable<?> elements = (Iterable<?>) attr.getValue(entity);
            if (elements != null) {
                for (final Object element : elements) {
                    executeUpdate(
                            em.getConnection(),
                            "INSERT INTO " + joinTable.getName() +
                            " (" + attr.getColumn().getName() + "," + attr.getColumn().getForeignReference().getColumnName() + ")" +
                            " VALUES " +
                            " (?,?)",
                            Arrays.asList(entityId, valueIdWrapper.getValue(element)));
                }
            }
        }
    }

    private <T> List<T> getResultList(final MinijaxEntityManager em, final MinijaxEntityType<T> resultType, final String sql, final List<Object> params) {
        final List<T> results = new ArrayList<>();
        LOG.debug("{}", sql);
        LOG.debug("{}", params);
        try (final PreparedStatement stmt = em.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                final Object param = params.get(i);
                Object value;
                if (param == null) {
                    value = null;
                } else if (param instanceof UUID) {
                    value = IdUtils.toBytes((UUID) param);
                } else {
                    value = param;
                }
                stmt.setObject(i + 1, value);
            }
            try (final ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(resultType.read(em, rs, new MutableInt(1)));
                }
            }
        } catch (final ReflectiveOperationException | SQLException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }

        LOG.debug("{} results", results.size());
        return results;
    }

    private int executeUpdate(final Connection conn, final String sql, final List<Object> params) {
        LOG.debug("{}", sql);
        LOG.debug("{}", params);
        try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                final Object param = params.get(i);
                Object value;
                if (param == null) {
                    value = null;
                } else if (param instanceof UUID) {
                    value = IdUtils.toBytes((UUID) param);
                } else {
                    value = param;
                }
                stmt.setObject(i + 1, value);
            }
            return stmt.executeUpdate();
        } catch (final SQLException ex) {
            LOG.error("SQLException: {}", ex.getMessage(), ex);
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }
}
