package org.minijax.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import jakarta.persistence.SynchronizationType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MinijaxEntityManagerFactoryTest {
    private MinijaxEntityManagerFactory emf;

    @BeforeEach
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }

    @Test
    @SuppressWarnings("rawtypes")
    void testCreateEntityManager1() {
        assertThrows(UnsupportedOperationException.class, () -> emf.createEntityManager((Map) null));
    }

    @Test
    void testCreateEntityManager2() {
        assertThrows(UnsupportedOperationException.class, () -> emf.createEntityManager((SynchronizationType) null));
    }

    @Test
    void testCreateEntityManager3() {
        assertThrows(UnsupportedOperationException.class, () -> emf.createEntityManager(null, null));
    }

    @Test
    void testGetCriteriaBuilder() {
        assertThrows(UnsupportedOperationException.class, () -> emf.getCriteriaBuilder());
    }

    @Test
    void testIsOpen() {
        assertThrows(UnsupportedOperationException.class, () -> emf.isOpen());
    }

    @Test
    void testGetCache() {
        assertThrows(UnsupportedOperationException.class, () -> emf.getCache());
    }

    @Test
    void testGetPersistenceUnitUtil() {
        assertThrows(UnsupportedOperationException.class, () -> emf.getPersistenceUnitUtil());
    }

    @Test
    void testaddNamedQuery() {
        assertThrows(UnsupportedOperationException.class, () -> emf.addNamedQuery(null, null));
    }

    @Test
    void testUnwrap() {
        assertThrows(UnsupportedOperationException.class, () -> emf.unwrap(null));
    }

    @Test
    void testaddNamedEntityGraph() {
        assertThrows(UnsupportedOperationException.class, () -> emf.addNamedEntityGraph(null, null));
    }
}
