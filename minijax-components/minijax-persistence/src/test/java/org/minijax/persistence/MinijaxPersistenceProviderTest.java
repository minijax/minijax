package org.minijax.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.spi.PersistenceUnitInfo;

import org.junit.jupiter.api.Test;

public class MinijaxPersistenceProviderTest {

    @Test
    public void testCreateEntityManagerFactory() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.createEntityManagerFactory("testdb", new HashMap<>());
    }

    @Test
    public void testCreateContainerEntityManagerFactory() {
        final Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.driver", "org.h2.jdbcx.JdbcDataSource");
        props.put("jakarta.persistence.jdbc.url", "jdbc:h2:mem:");

        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.createContainerEntityManagerFactory(new MinijaxPersistenceUnitInfo("testdb", ""), props);
    }

    @Test
    public void testGenerateSchema1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.generateSchema("", null);
    });
    }

    @Test
    public void testGenerateSchema2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.generateSchema((PersistenceUnitInfo) null, null);
    });
    }

    @Test
    public void testGetProviderUtil() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.getProviderUtil();
    });
    }
}
