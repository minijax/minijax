package org.minijax.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.spi.PersistenceUnitInfo;

import org.junit.jupiter.api.Test;

class MinijaxPersistenceProviderTest {

    @Test
    void testCreateEntityManagerFactory() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.createEntityManagerFactory("testdb", new HashMap<>());
    }

    @Test
    void testCreateContainerEntityManagerFactory() {
        final Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.driver", "org.h2.jdbcx.JdbcDataSource");
        props.put("jakarta.persistence.jdbc.url", "jdbc:h2:mem:");

        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.createContainerEntityManagerFactory(new MinijaxPersistenceUnitInfo("testdb", ""), props);
    }

    @Test
    void testGenerateSchema1() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.generateSchema("", null);
    });
    }

    @Test
    void testGenerateSchema2() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.generateSchema((PersistenceUnitInfo) null, null);
    });
    }

    @Test
    void testGetProviderUtil() {
        assertThrows(UnsupportedOperationException.class, () -> {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.getProviderUtil();
    });
    }
}
