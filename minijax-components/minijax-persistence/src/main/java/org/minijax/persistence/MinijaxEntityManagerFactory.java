package org.minijax.persistence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.Entity;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;

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
        this.metamodel = new MinijaxMetamodel.Builder(unitInfo).build();
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
        runInitScript();
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
        // TODO: Close database connection pools once implemented
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
        try (final Connection conn = createConnection()) {
            for (final String className : unitInfo.getManagedClassNames()) {
                createTableForClass(conn, className);
            }
        } catch (final SQLException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    void createTableForClass(final Connection conn, final String className) {
        final Class<?> cls;
        try {
            cls = Class.forName(className);
        } catch (final ClassNotFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }

        if (cls.getAnnotation(Entity.class) != null) {
            dialect.createTables(conn, metamodel.entity(cls));
        }
    }

    void runInitScript() {
        final String initScript = (String) properties.get("javax.persistence.sql-load-script-source");
        if (initScript == null || initScript.isBlank()) {
            return;
        }

        try (final Connection conn = createConnection();
                final Statement stmt = conn.createStatement()) {

            final URL url = MinijaxEntityManagerFactory.class.getClassLoader().getResource(initScript);
            final Path path = Paths.get(url.toURI());
            final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (final String line : lines) {
                LOG.debug(line);
                stmt.executeUpdate(line);
            }

            conn.commit();

        } catch (final SQLException | IOException | URISyntaxException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    Connection createConnection() {
        try {
            // Sonar warns that the connection should be closed.
            // Obviously we do not want to close the connection,
            // because we are returning it to a consumer.
            @SuppressWarnings("java:S2095")
            final Connection result = DriverManager.getConnection(url, user, password);
            result.setAutoCommit(false);
            return result;
        } catch (final SQLException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
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
