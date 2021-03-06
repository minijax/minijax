package org.minijax.liquibase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import jakarta.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Test;
import org.minijax.commons.MinijaxProperties;

import liquibase.change.core.AddColumnChange;
import liquibase.change.core.DropTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.resource.FileSystemResourceAccessor;

class LiquibaseHelperTest {
    private static final File TEST_RESOURCES = new File("src/test/resources");

    @Test
    void testDefaultConstructor() {
        final Map<String, String> props = new HashMap<>();
        final LiquibaseHelper h = new LiquibaseHelper(props);

        assertEquals("resources", h.getResourcesDir().getName());
        assertEquals("master.changelog.xml", h.getMasterChangeLogFile().getName());
    }

    @Test
    void testEndToEnd() throws Exception {
        final File targetFile = File.createTempFile("target", null);
        final File referenceFile = File.createTempFile("reference", null);
        final String targetUrl = "jdbc:h2:" + targetFile.getAbsolutePath();
        final String referenceUrl = "jdbc:h2:" + referenceFile.getAbsolutePath();
        final String masterChangeLogResourceName = "changes-" + System.currentTimeMillis() + ".xml";
        final File migrationsDir = new File(TEST_RESOURCES, "migrations");
        final File masterChangeLogFile = new File(migrationsDir, masterChangeLogResourceName);

        final Map<String, String> props = new HashMap<>();
        props.put(MinijaxProperties.PERSISTENCE_UNIT_NAME, "testdb");
        props.put(MinijaxProperties.DB_DRIVER, "org.h2.jdbcx.JdbcDataSource");
        props.put(MinijaxProperties.DB_URL, targetUrl);
        props.put(MinijaxProperties.DB_REFERENCE_URL, referenceUrl);
        props.put(MinijaxProperties.DB_USERNAME, "");
        props.put(MinijaxProperties.DB_PASSWORD, "");

        // Verify that both databases are empty and that the changelog does not exist
        assertTrue(getTables(targetUrl).isEmpty());
        assertTrue(getTables(referenceUrl).isEmpty());
        assertFalse(masterChangeLogFile.exists());

        final LiquibaseHelper m = new LiquibaseHelper(
                props,
                new FileSystemResourceAccessor(TEST_RESOURCES),
                TEST_RESOURCES,
                masterChangeLogResourceName);

        // Generate the migration
        final File firstGeneratedChangeLog = m.generateMigrations();
        assertTrue(firstGeneratedChangeLog.exists());

        // Verify that the "target" database is still empty
        assertTrue(getTables(targetUrl).isEmpty());

        // Verify that the "reference" database now has the "WIDGET" table
        assertEquals(Collections.singletonList("WIDGET"), getTables(referenceUrl));

        // Verify that the changelog file now exists
        assertTrue(masterChangeLogFile.exists());

        // Run the migration
        m.migrate();

        // Verify that the "target" database now has Liquibase tables and the "WIDGET" table
        assertEquals(Arrays.asList("DATABASECHANGELOG", "DATABASECHANGELOGLOCK", "WIDGET"), getTables(targetUrl));

        // Generate the migration again (should be no-op)
        final File secondGeneratedChangeLog = m.generateMigrations();
        assertNull(secondGeneratedChangeLog);

        // Verify that the "reference" database is still only the "WIDGET" table
        assertEquals(Collections.singletonList("WIDGET"), getTables(referenceUrl));

        // Run the migration again (should be no-op)
        m.migrate();

        // Verify that the "target" database still has only Liquibase tables and the "WIDGET" table
        assertEquals(Arrays.asList("DATABASECHANGELOG", "DATABASECHANGELOGLOCK", "WIDGET"), getTables(targetUrl));

        // Cleanup
        deleteDatabase(targetFile);
        deleteDatabase(referenceFile);
        masterChangeLogFile.delete();
        firstGeneratedChangeLog.delete();
        migrationsDir.delete();
    }

    @Test
    void testNotIgnoreEmptyChangeSet() {
        final ChangeSet changeSet = new ChangeSet(null);
        assertFalse(LiquibaseHelper.isIgnoredChangeSet(changeSet));
    }

    @Test
    void testNotIgnoreAddColumn() {
        final AddColumnChange change = new AddColumnChange();

        final ChangeSet changeSet = new ChangeSet(null);
        changeSet.addChange(change);

        assertFalse(LiquibaseHelper.isIgnoredChangeSet(changeSet));
    }

    @Test
    void testNotIgnoreDropOtherTable() {
        final DropTableChange change = new DropTableChange();
        change.setTableName("foo");

        final ChangeSet changeSet = new ChangeSet(null);
        changeSet.addChange(change);

        assertFalse(LiquibaseHelper.isIgnoredChangeSet(changeSet));
    }

    @Test
    void testIgnoreChangeSet() {
        final DropTableChange change = new DropTableChange();
        change.setTableName("JGROUPSPING");

        final ChangeSet changeSet = new ChangeSet(null);
        changeSet.addChange(change);

        assertTrue(LiquibaseHelper.isIgnoredChangeSet(changeSet));
    }

    @Test
    void testFilterChangeSets() {
        final DropTableChange c1 = new DropTableChange();
        c1.setTableName("foo");

        final ChangeSet cs1 = new ChangeSet(null);
        cs1.addChange(c1);

        final DropTableChange c2 = new DropTableChange();
        c2.setTableName("JGROUPSPING");

        final ChangeSet cs2 = new ChangeSet(null);
        cs2.addChange(c2);

        final List<ChangeSet> original = Arrays.asList(cs1, cs2);
        final List<ChangeSet> filtered = LiquibaseHelper.filterChangeSets(original);
        assertEquals(1, filtered.size());
        assertEquals(c1, filtered.get(0).getChanges().get(0));
    }

    @Test
    void testCloseEntityManagerFactoryNull() {
        LiquibaseHelper.closeQuietly((EntityManagerFactory) null);
    }

    @Test
    void testCloseEntityManagerFactoryException() throws Exception {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        doThrow(new RuntimeException("Boom")).when(emf).close();

        LiquibaseHelper.closeQuietly(emf);

        verify(emf).close();
    }

    @Test
    void testCloseDatabaseNull() {
        LiquibaseHelper.closeQuietly((Database) null);
    }

    @Test
    void testCloseDatabaseException() throws Exception {
        final Database database = mock(Database.class);
        doThrow(new RuntimeException("Boom")).when(database).close();

        LiquibaseHelper.closeQuietly(database);

        verify(database).close();
    }

    private static List<String> getTables(final String url) throws SQLException {
        final List<String> result = new ArrayList<>();
        try (final Connection c = DriverManager.getConnection(url);
                final Statement s = c.createStatement();
                final ResultSet rs = s.executeQuery("SHOW TABLES")) {
            while (rs.next()) {
                result.add(rs.getString("TABLE_NAME"));
            }
        }
        return result;
    }

    private static void deleteDatabase(final File dbFile) throws IOException {
        final String name = dbFile.getName();
        final File parent = dbFile.getCanonicalFile().getParentFile();
        for (final File child : parent.listFiles()) {
            if (child.getName().startsWith(name)) {
                child.delete();
            }
        }
    }
}
