package org.minijax.persistence;

import org.junit.Before;
import org.junit.Test;

public class MinijaxPersistenceUnitInfoTest {
    private MinijaxPersistenceUnitInfo pui;

    @Before
    public void setUp() {
        pui = new MinijaxPersistenceUnitInfo("", "");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTransactionType() {
        pui.getTransactionType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetJtaDataSource() {
        pui.getJtaDataSource();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetNonJtaDataSource() {
        pui.getNonJtaDataSource();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMappingFileNames() {
        pui.getMappingFileNames();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetJarFileUrls() {
        pui.getJarFileUrls();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPersistenceUnitRootUrl() {
        pui.getPersistenceUnitRootUrl();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExcludeUnlistedClasses() {
        pui.excludeUnlistedClasses();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSharedCacheMode() {
        pui.getSharedCacheMode();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValidationMode() {
        pui.getValidationMode();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPersistenceXMLSchemaVersion() {
        pui.getPersistenceXMLSchemaVersion();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetClassLoader() {
        pui.getClassLoader();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddTransformer() {
        pui.addTransformer(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetNewTempClassLoader() {
        pui.getNewTempClassLoader();
    }
}
