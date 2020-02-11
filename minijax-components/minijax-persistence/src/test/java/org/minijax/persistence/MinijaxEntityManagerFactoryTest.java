package org.minijax.persistence;

import java.util.Map;

import javax.persistence.SynchronizationType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MinijaxEntityManagerFactoryTest {
    private MinijaxEntityManagerFactory emf;

    @Before
    public void setUp() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        emf = provider.createEntityManagerFactory("testdb", null);
    }

    @After
    public void tearDown() {
        emf.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("rawtypes")
    public void testCreateEntityManager1() {
        emf.createEntityManager((Map) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateEntityManager2() {
        emf.createEntityManager((SynchronizationType) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("rawtypes")
    public void testCreateEntityManager3() {
        emf.createEntityManager((SynchronizationType) null, (Map) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCriteriaBuilder() {
        emf.getCriteriaBuilder();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsOpen() {
        emf.isOpen();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCache() {
        emf.getCache();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPersistenceUnitUtil() {
        emf.getPersistenceUnitUtil();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testaddNamedQuery() {
        emf.addNamedQuery(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnwrap() {
        emf.unwrap(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testaddNamedEntityGraph() {
        emf.addNamedEntityGraph(null, null);
    }
}
