package org.minijax.orm;

import javax.persistence.spi.PersistenceUnitInfo;

import org.junit.Test;

public class MinijaxPersistenceProviderTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateEntityManagerFactory() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.createEntityManagerFactory("", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateContainerEntityManagerFactory() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.createContainerEntityManagerFactory(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGenerateSchema1() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.generateSchema("", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGenerateSchema2() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.generateSchema((PersistenceUnitInfo) null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetProviderUtil() {
        final MinijaxPersistenceProvider provider = new MinijaxPersistenceProvider();
        provider.getProviderUtil();
    }
}
