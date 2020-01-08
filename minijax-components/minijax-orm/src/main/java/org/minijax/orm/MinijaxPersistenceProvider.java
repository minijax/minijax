package org.minijax.orm;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

public class MinijaxPersistenceProvider implements javax.persistence.spi.PersistenceProvider {

    @Override
    @SuppressWarnings("rawtypes")
    public EntityManagerFactory createEntityManagerFactory(final String emName, final Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public EntityManagerFactory createContainerEntityManagerFactory(final PersistenceUnitInfo info, final Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void generateSchema(final PersistenceUnitInfo info, final Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean generateSchema(final String persistenceUnitName, final Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProviderUtil getProviderUtil() {
        throw new UnsupportedOperationException();
    }
}
