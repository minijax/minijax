package org.minijax.persistence;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

public class MinijaxPersistenceUnitInfo implements jakarta.persistence.spi.PersistenceUnitInfo {
    private final String name;
    private final String providerClassName;
    private final List<String> classNames;
    private final Properties properties;

    public MinijaxPersistenceUnitInfo(final String name, final String providerClassName) {
        this.name = name;
        this.providerClassName = providerClassName;

        classNames = new ArrayList<>();
        properties = new Properties();
    }

    @Override
    public String getPersistenceUnitName() {
        return name;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return providerClassName;
    }

    @Override
    public List<String> getManagedClassNames() {
        return classNames;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    /*
     * Unsupported
     */

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataSource getJtaDataSource() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataSource getNonJtaDataSource() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getMappingFileNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<URL> getJarFileUrls() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean excludeUnlistedClasses() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidationMode getValidationMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addTransformer(final ClassTransformer transformer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        throw new UnsupportedOperationException();
    }
}
