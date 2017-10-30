package org.minijax.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersistenceUtilsTest {

    @Test
    public void testNotFound() {
        assertNull(PersistenceUtils.getDefaultName("not-found.xml"));
    }

    @Test
    public void testOne() {
        assertEquals("testdb", PersistenceUtils.getDefaultName("test-persistence1.xml"));
    }

    @Test
    public void testMultiple() {
        assertEquals("test1", PersistenceUtils.getDefaultName("test-persistence2.xml"));
    }
}
