package org.minijax.data;

import java.util.Map;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;
import org.minijax.entity.UuidGenerator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class EclipselinkSessionCustomizer implements SessionCustomizer {

    @Override
    public void customize(final Session session) {
        final DatabaseLogin login = session.getLogin();
        login.addSequence(new UuidGenerator());
        setupDataSource(session);
    }

    /**
     * Returns a HikariCP data source using the JDBC connection values.
     *
     * EclipseLink has a built-in connection pool, but unfortunately is lacks "validate on borrow".
     * Plus, HikariCP has famously better performance.
     *
     * The props are the combination of the following:
     * 1) The values from persistence.xml
     * 2) Override values from the optional 2nd arg to <code>Persistence.createEntityManagerFactory</code>
     *
     * @param props Connection properties.
     * @return A new HikariCP data source.
     */
    private static void setupDataSource(final Session session) {
        final DatabaseLogin login = session.getLogin();
        final Map<Object, Object> props = session.getProperties();

        final String url = (String) props.get("org.minijax.data.url");
        if (url == null) {
            return;
        }

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setDriverClassName((String) props.get("org.minijax.data.driver"));
        config.setUsername((String) props.get("org.minijax.data.username"));
        config.setPassword((String) props.get("org.minijax.data.password"));

        // Recommended performance settings from HikariCP:
        // https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        login.setConnector(new JNDIConnector(new HikariDataSource(config)));
        login.useExternalConnectionPooling();
    }
}
