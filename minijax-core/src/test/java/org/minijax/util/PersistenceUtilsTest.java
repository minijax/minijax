package org.minijax.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class PersistenceUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new PersistenceUtils();
    }

    @Test
    public void testNotFound() {
        assertEquals(Collections.emptyList(), PersistenceUtils.getNames("not-found.xml"));
    }

    @Test
    public void testNotXml() {
        assertEquals(Collections.emptyList(), PersistenceUtils.getNames("config.properties"));
    }

    @Test
    public void testOne() {
        assertEquals(Collections.singletonList("testdb"), PersistenceUtils.getNames("test-persistence1.xml"));
    }

    @Test
    public void testMultiple() {
        assertEquals(Arrays.asList("test1", "test2"), PersistenceUtils.getNames("test-persistence2.xml"));
    }
}
