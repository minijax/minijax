package org.minijax.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.Entity;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;

import org.minijax.commons.MinijaxException;
import org.minijax.persistence.dialect.AnsiSqlDialect;
import org.minijax.persistence.dialect.SqlDialect;
import org.minijax.persistence.metamodel.MinijaxMetamodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxEntityManagerFactory implements javax.persistence.EntityManagerFactory, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxEntityManagerFactory.class);
    private final MinijaxPersistenceUnitInfo unitInfo;
    private final MinijaxMetamodel metamodel;
    private final Map<String, Object> properties;
    private final SqlDialect dialect;
    private final String url;
    private final String user;
    private final String password;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MinijaxEntityManagerFactory(final MinijaxPersistenceUnitInfo unitInfo, final Map<String, Object> properties) {
        this.unitInfo = unitInfo;
        this.metamodel = new MinijaxMetamodel();
        this.properties = new HashMap<>();

        if (unitInfo.getProperties() != null) {
            this.properties.putAll(((Map) unitInfo.getProperties()));
        }

        if (properties != null) {
            this.properties.putAll(properties);
        }

        this.dialect = new AnsiSqlDialect();

        this.url = (String) this.properties.getOrDefault("javax.persistence.jdbc.url", "");
        this.user = (String) this.properties.getOrDefault("javax.persistence.jdbc.user", "");
        this.password = (String) this.properties.getOrDefault("javax.persistence.jdbc.password", "");
        loadDriver();
        createTables();
    }

    public MinijaxPersistenceUnitInfo getPersistenceUnitInfo() {
        return unitInfo;
    }

    @Override
    public MinijaxMetamodel getMetamodel() {
        return metamodel;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public MinijaxEntityManager createEntityManager() {
        return new MinijaxEntityManager(this, metamodel, dialect, createConnection());
    }

    @Override
    public void close() {
    }

    /*
     * Private helpers
     */

    void loadDriver() {
        final String driverClassName = (String) properties.get("javax.persistence.jdbc.driver");
        if (driverClassName == null) {
            return;
        }

        try {
            Class.forName(driverClassName).getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | SecurityException ex) {
            LOG.error("Could not load JDBC driver: {}", ex.getMessage(), ex);
        }
    }

    void createTables() {
        try (final MinijaxEntityManager em = createEntityManager()) {
            for (final String className : unitInfo.getManagedClassNames()) {
                createTableForClass(em, className);
            }
        }
    }

    void createTableForClass(final MinijaxEntityManager em, final String className) {
        final Class<?> cls;
        try {
            cls = Class.forName(className);
        } catch (final ClassNotFoundException ex) {
            LOG.error("Error creating tables: {}", ex.getMessage(), ex);
            throw new MinijaxException(ex.getMessage(), ex);
        }

        if (cls.getAnnotation(Entity.class) != null) {
            dialect.createTables(em, cls);
        }
    }

    Connection createConnection() {
        try {
            // Sonar warns that the connection should be closed.
            // Obviously we do not want to close the connection, because we are returning it to a consumer.
            @SuppressWarnings("java:S2095")
            final Connection result = DriverManager.getConnection(url, user, password);
            result.setAutoCommit(false);
            return result;
        } catch (final SQLException ex) {
            LOG.error("Could not get JDBC connection: {}", ex.getMessage(), ex);
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }

    /*
     * Unsupported
     */

    @Override
    @SuppressWarnings("rawtypes")
    public EntityManager createEntityManager(final Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityManager createEntityManager(final SynchronizationType synchronizationType) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public EntityManager createEntityManager(final SynchronizationType synchronizationType, final Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cache getCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addNamedQuery(final String name, final Query query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(final Class<T> cls) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void addNamedEntityGraph(final String graphName, final EntityGraph<T> entityGraph) {
        throw new UnsupportedOperationException();
    }
}
