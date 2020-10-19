package org.minijax.persistence;

import static org.junit.Assert.*;

import org.junit.Test;

public class MinijaxPersistenceFileTest {

    @Test
    public void testReadFile() {
        final MinijaxPersistenceFile file = MinijaxPersistenceFile.read("META-INF/persistence.xml");
        assertNotNull(file);

        final MinijaxPersistenceUnitInfo unitInfo = file.getPersistenceUnit("testdb");
        assertNotNull(unitInfo);
        assertEquals("testdb", unitInfo.getPersistenceUnitName());
        assertEquals("org.h2.jdbcx.JdbcDataSource", unitInfo.getProperties().get("jakarta.persistence.jdbc.driver"));
        assertEquals("drop-and-create", unitInfo.getProperties().get("jakarta.persistence.schema-generation.database.action"));
    }
}
