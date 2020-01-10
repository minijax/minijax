package org.minijax.persistence;

import java.util.Map;

import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

import org.minijax.commons.MinijaxException;

public class MinijaxPersistenceProvider implements javax.persistence.spi.PersistenceProvider {
    private final MinijaxPersistenceFile persistenceFile;

    public MinijaxPersistenceProvider() {
        persistenceFile = MinijaxPersistenceFile.read("META-INF/persistence.xml");
    }

    @Override
    @SuppressWarnings("rawtypes")
    public MinijaxEntityManagerFactory createEntityManagerFactory(final String emName, final Map map) {
        final MinijaxPersistenceUnitInfo unit = persistenceFile.getPersistenceUnit(emName);
        if (unit == null) {
            throw new MinijaxException("Persistence unit not found (\"" + emName + "\")");
        }
        return createContainerEntityManagerFactory(unit, map);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public MinijaxEntityManagerFactory createContainerEntityManagerFactory(final PersistenceUnitInfo info, final Map map) {
        return new MinijaxEntityManagerFactory((MinijaxPersistenceUnitInfo) info, map);
    }

    /*
     * Unsupported
     */

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
