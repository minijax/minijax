package org.minijax.liquibase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.minijax.data.DataProperties;
import org.mockito.junit.MockitoJUnitRunner;

import liquibase.change.core.AddColumnChange;
import liquibase.change.core.DropTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.resource.FileSystemResourceAccessor;

@RunWith(MockitoJUnitRunner.class)
public class LiquibaseHelperTest {
    private static final File TEST_RESOURCES = new File("src/test/resources");

    @Test
    public void testDefaultConstructor() {
        final Map<String, String> props = new HashMap<>();
        final LiquibaseHelper h = new LiquibaseHelper(props);

        assertEquals("resources", h.getResourcesDir().getName());
        assertEquals("changelog.xml", h.getChangeLogResourceName());
    }

    @Test
    public void testEndToEnd() throws Exception {
        final File targetFile = File.createTempFile("target", null);
        final File referenceFile = File.createTempFile("reference", null);
        final String targetUrl = "jdbc:h2:" + targetFile.getAbsolutePath();
        final String referenceUrl = "jdbc:h2:" + referenceFile.getAbsolutePath();
        final String changeLogResourceName = "changes-" + System.currentTimeMillis() + ".xml";
        final File changeLogFile = new File(TEST_RESOURCES, changeLogResourceName);

        final Map<String, String> props = new HashMap<>();
        props.put(DataProperties.PERSISTENCE_UNIT_NAME, "testdb");
        props.put(DataProperties.DRIVER, "org.h2.jdbcx.JdbcDataSource");
        props.put(DataProperties.URL, targetUrl);
        props.put(DataProperties.REFERENCE_URL, referenceUrl);
        props.put(DataProperties.USERNAME, "");
        props.put(DataProperties.PASSWORD, "");

        // Verify that both databases are empty and that the changelog does not exist
        assertTrue(getTables(targetUrl).isEmpty());
        assertTrue(getTables(referenceUrl).isEmpty());
        assertFalse(changeLogFile.exists());

        final LiquibaseHelper m = new LiquibaseHelper(
                props,
                new FileSystemResourceAccessor("src/test/resources"),
                TEST_RESOURCES,
                changeLogResourceName);

        // Generate the migration
        m.generateMigrations();

        // Verify that the "target" database is still empty
        assertTrue(getTables(targetUrl).isEmpty());

        // Verify that the "reference" database now has the "WIDGET" table
        assertEquals(Arrays.asList("WIDGET"), getTables(referenceUrl));

        // Verify that the changelog file now exists
        assertTrue(changeLogFile.exists());

        // Run the migration
        m.migrate();

        // Verify that the "target" database now has Liquibase tables and the "WIDGET" table
        assertEquals(Arrays.asList("DATABASECHANGELOG", "DATABASECHANGELOGLOCK", "WIDGET"), getTables(targetUrl));

        // Generate the migration again (should be no-op)
        m.generateMigrations();

        // Verify that the "reference" database is still only the "WIDGET" table
        assertEquals(Arrays.asList("WIDGET"), getTables(referenceUrl));

        // Run the migration again (should be no-op)
        m.migrate();

        // Verify that the "target" database still has only Liquibase tables and the "WIDGET" table
        assertEquals(Arrays.asList("DATABASECHANGELOG", "DATABASECHANGELOGLOCK", "WIDGET"), getTables(targetUrl));

        // Cleanup
        deleteDatabase(targetFile);
        deleteDatabase(referenceFile);
        changeLogFile.delete();
    }


    @Test
    public void testAuthorName() {
        assertNull(LiquibaseHelper.getAuthor(null));
        assertNull(LiquibaseHelper.getAuthor(""));
        assertNull(LiquibaseHelper.getAuthor(" "));
        assertEquals("Cody", LiquibaseHelper.getAuthor("cody"));
    }


    @Test
    public void testNotIgnoreEmptyChangeSet() {
        final ChangeSet changeSet = new ChangeSet(null);
        assertFalse(LiquibaseHelper.isIgnoredChangeSet(changeSet));
    }


    @Test
    public void testNotIgnoreAddColumn() {
        final AddColumnChange change = new AddColumnChange();

        final ChangeSet changeSet = new ChangeSet(null);
        changeSet.addChange(change);

        assertFalse(LiquibaseHelper.isIgnoredChangeSet(changeSet));
    }


    @Test
    public void testNotIgnoreDropOtherTable() {
        final DropTableChange change = new DropTableChange();
        change.setTableName("foo");

        final ChangeSet changeSet = new ChangeSet(null);
        changeSet.addChange(change);

        assertFalse(LiquibaseHelper.isIgnoredChangeSet(changeSet));
    }


    @Test
    public void testIgnoreChangeSet() {
        final DropTableChange change = new DropTableChange();
        change.setTableName("JGROUPSPING");

        final ChangeSet changeSet = new ChangeSet(null);
        changeSet.addChange(change);

        assertTrue(LiquibaseHelper.isIgnoredChangeSet(changeSet));
    }


    @Test
    public void testCloseEntityManagerFactoryNull() {
        LiquibaseHelper.closeQuietly((EntityManagerFactory) null);
    }


    @Test
    public void testCloseEntityManagerFactoryException() throws Exception {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        doThrow(new RuntimeException("Boom")).when(emf).close();

        LiquibaseHelper.closeQuietly(emf);

        verify(emf).close();
    }


    @Test
    public void testCloseDatabaseNull() {
        LiquibaseHelper.closeQuietly((Database) null);
    }


    @Test
    public void testCloseDatabaseException() throws Exception {
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
