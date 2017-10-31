package org.minijax.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class PersistenceUtilsTest {

    @Test
    public void testNotFound() {
        assertEquals(Collections.emptyList(), PersistenceUtils.getNames("not-found.xml"));
    }

    @Test
    public void testOne() {
        assertEquals(Arrays.asList("testdb"), PersistenceUtils.getNames("test-persistence1.xml"));
    }

    @Test
    public void testMultiple() {
        assertEquals(Arrays.asList("test1", "test2"), PersistenceUtils.getNames("test-persistence2.xml"));
    }
}
