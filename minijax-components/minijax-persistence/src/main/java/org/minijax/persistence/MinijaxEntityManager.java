package org.minijax.persistence;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;

import org.minijax.commons.CloseUtils;
import org.minijax.persistence.criteria.MinijaxCriteriaBuilder;
import org.minijax.persistence.criteria.MinijaxCriteriaQuery;
import org.minijax.persistence.criteria.MinijaxRoot;
import org.minijax.persistence.dialect.SqlDialect;
import org.minijax.persistence.jpql.Parser;
import org.minijax.persistence.jpql.Tokenizer;
import org.minijax.persistence.metamodel.MinijaxMetamodel;

public class MinijaxEntityManager implements javax.persistence.EntityManager, AutoCloseable {
    private final MinijaxEntityManagerFactory emf;
    private final MinijaxMetamodel metamodel;
    private final SqlDialect dialect;
    private final Connection connection;
    private final MinijaxTransaction transaction;

    public MinijaxEntityManager(
            final MinijaxEntityManagerFactory emf,
            final MinijaxMetamodel metamodel,
            final SqlDialect dialect,
            final Connection connection) {

        this.emf = Objects.requireNonNull(emf);
        this.metamodel = Objects.requireNonNull(metamodel);
        this.dialect = Objects.requireNonNull(dialect);
        this.connection = Objects.requireNonNull(connection);
        this.transaction = new MinijaxTransaction(connection);
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @Override
    public MinijaxMetamodel getMetamodel() {
        return metamodel;
    }

    public SqlDialect getDialect() {
        return dialect;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public EntityTransaction getTransaction() {
        return transaction;
    }

    @Override
    public void persist(final Object entity) {
        dialect.persist(this, entity);
    }

    @Override
    public <T> T merge(final T entity) {
        return dialect.merge(this, entity);
    }

    @Override
    public void remove(final Object entity) {
        dialect.remove(this, entity);
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey) {
        final MinijaxCriteriaBuilder cb = getCriteriaBuilder();
        final MinijaxCriteriaQuery<T> q = cb.createQuery(entityClass);
        final MinijaxRoot<T> w = q.from(entityClass);
        q.select(w).where(cb.equal(w.get("ID"), primaryKey));
        return createQuery(q).getSingleResult();
    }

    @Override
    public <T> MinijaxQuery<T> createQuery(final String qlString, final Class<T> resultClass) {
        return new MinijaxQuery<>(this, Parser.parse(getCriteriaBuilder(), resultClass, Tokenizer.tokenize(qlString)));
    }

    @Override
    public <T> MinijaxQuery<T> createQuery(final CriteriaQuery<T> criteriaQuery) {
        return new MinijaxQuery<>(this, (MinijaxCriteriaQuery<T>) criteriaQuery);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
        CloseUtils.closeQuietly(connection);
    }

    @Override
    public MinijaxCriteriaBuilder getCriteriaBuilder() {
        return new MinijaxCriteriaBuilder(this);
    }

    /*
     * Unsupported
     */

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode, final Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getReference(final Class<T> entityClass, final Object primaryKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFlushMode(final FlushModeType flushMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FlushModeType getFlushMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lock(final Object entity, final LockModeType lockMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lock(final Object entity, final LockModeType lockMode, final Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(final Object entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(final Object entity, final Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(final Object entity, final LockModeType lockMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refresh(final Object entity, final LockModeType lockMode, final Map<String, Object> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void detach(final Object entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(final Object entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LockModeType getLockMode(final Object entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setProperty(final String propertyName, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createQuery(final String qlString) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Query createQuery(final CriteriaUpdate updateQuery) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Query createQuery(final CriteriaDelete deleteQuery) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNamedQuery(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(final String name, final Class<T> resultClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNativeQuery(final String sqlString) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Query createNativeQuery(final String sqlString, final Class resultClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNativeQuery(final String sqlString, final String resultSetMapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName, final Class... resultClasses) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName, final String... resultSetMappings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void joinTransaction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isJoinedToTransaction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(final Class<T> cls) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getDelegate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(final Class<T> rootType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityGraph<?> createEntityGraph(final String graphName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityGraph<?> getEntityGraph(final String graphName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(final Class<T> entityClass) {
        throw new UnsupportedOperationException();
    }
}
