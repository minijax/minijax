package org.minijax.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MinijaxPersistenceUnitInfoTest {
    private MinijaxPersistenceUnitInfo pui;

    @BeforeEach
    public void setUp() {
        pui = new MinijaxPersistenceUnitInfo("", "");
    }

    @Test
    public void testGetTransactionType() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getTransactionType());
    }

    @Test
    public void testGetJtaDataSource() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getJtaDataSource());
    }

    @Test
    public void testGetNonJtaDataSource() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getNonJtaDataSource());
    }

    @Test
    public void testGetMappingFileNames() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getMappingFileNames());
    }

    @Test
    public void testGetJarFileUrls() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getJarFileUrls());
    }

    @Test
    public void testGetPersistenceUnitRootUrl() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getPersistenceUnitRootUrl());
    }

    @Test
    public void testExcludeUnlistedClasses() {
        assertThrows(UnsupportedOperationException.class, () -> pui.excludeUnlistedClasses());
    }

    @Test
    public void testGetSharedCacheMode() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getSharedCacheMode());
    }

    @Test
    public void testGetValidationMode() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getValidationMode());
    }

    @Test
    public void testGetPersistenceXMLSchemaVersion() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getPersistenceXMLSchemaVersion());
    }

    @Test
    public void testGetClassLoader() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getClassLoader());
    }

    @Test
    public void testAddTransformer() {
        assertThrows(UnsupportedOperationException.class, () -> pui.addTransformer(null));
    }

    @Test
    public void testGetNewTempClassLoader() {
        assertThrows(UnsupportedOperationException.class, () -> pui.getNewTempClassLoader());
    }
}
