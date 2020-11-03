package org.minijax.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import jakarta.persistence.SynchronizationType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MinijaxEntityManagerFactoryTest {
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
    public void testCreateEntityManager1() {
        assertThrows(UnsupportedOperationException.class, () -> emf.createEntityManager((Map) null));
    }

    @Test
    public void testCreateEntityManager2() {
        assertThrows(UnsupportedOperationException.class, () -> emf.createEntityManager((SynchronizationType) null));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testCreateEntityManager3() {
        assertThrows(UnsupportedOperationException.class, () -> emf.createEntityManager((SynchronizationType) null, (Map) null));
    }

    @Test
    public void testGetCriteriaBuilder() {
        assertThrows(UnsupportedOperationException.class, () -> emf.getCriteriaBuilder());
    }

    @Test
    public void testIsOpen() {
        assertThrows(UnsupportedOperationException.class, () -> emf.isOpen());
    }

    @Test
    public void testGetCache() {
        assertThrows(UnsupportedOperationException.class, () -> emf.getCache());
    }

    @Test
    public void testGetPersistenceUnitUtil() {
        assertThrows(UnsupportedOperationException.class, () -> emf.getPersistenceUnitUtil());
    }

    @Test
    public void testaddNamedQuery() {
        assertThrows(UnsupportedOperationException.class, () -> emf.addNamedQuery(null, null));
    }

    @Test
    public void testUnwrap() {
        assertThrows(UnsupportedOperationException.class, () -> emf.unwrap(null));
    }

    @Test
    public void testaddNamedEntityGraph() {
        assertThrows(UnsupportedOperationException.class, () -> emf.addNamedEntityGraph(null, null));
    }
}
