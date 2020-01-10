package org.minijax.persistence;

import static org.junit.Assert.*;

import org.junit.Test;
import org.minijax.persistence.MinijaxPersistenceFile;
import org.minijax.persistence.MinijaxPersistenceUnitInfo;

public class MinijaxPersistenceFileTest {

    @Test
    public void testReadFile() {
        final MinijaxPersistenceFile file = MinijaxPersistenceFile.read("META-INF/persistence.xml");
        assertNotNull(file);

        final MinijaxPersistenceUnitInfo unitInfo = file.getPersistenceUnit("testdb");
        assertNotNull(unitInfo);
        assertEquals("testdb", unitInfo.getPersistenceUnitName());
        assertEquals("org.h2.jdbcx.JdbcDataSource", unitInfo.getProperties().get("javax.persistence.jdbc.driver"));
        assertEquals("drop-and-create", unitInfo.getProperties().get("javax.persistence.schema-generation.database.action"));
    }
}
