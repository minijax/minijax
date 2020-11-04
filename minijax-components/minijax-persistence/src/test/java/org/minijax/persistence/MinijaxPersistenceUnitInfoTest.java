package org.minijax.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MinijaxPersistenceUnitInfoTest {
    private MinijaxPersistenceUnitInfo pui;

    @BeforeEach
    public void setUp() {
        pui = new MinijaxPersistenceUnitInfo("", "");
    }

    @Test
    void testGetTransactionType() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getTransactionType());
    }

    @Test
    void testGetJtaDataSource() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getJtaDataSource());
    }

    @Test
    void testGetNonJtaDataSource() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getNonJtaDataSource());
    }

    @Test
    void testGetMappingFileNames() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getMappingFileNames());
    }

    @Test
    void testGetJarFileUrls() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getJarFileUrls());
    }

    @Test
    void testGetPersistenceUnitRootUrl() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getPersistenceUnitRootUrl());
    }

    @Test
    void testExcludeUnlistedClasses() {
        assertThrows(UnsupportedOperationException.class, () -> pui.excludeUnlistedClasses());
    }

    @Test
    void testGetSharedCacheMode() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getSharedCacheMode());
    }

    @Test
    void testGetValidationMode() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getValidationMode());
    }

    @Test
    void testGetPersistenceXMLSchemaVersion() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getPersistenceXMLSchemaVersion());
    }

    @Test
    void testGetClassLoader() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getClassLoader());
    }

    @Test
    void testAddTransformer() {
        assertThrows(UnsupportedOperationException.class, () -> pui.addTransformer(null));
    }

    @Test
    void testGetNewTempClassLoader() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getNewTempClassLoader());
    }
}
