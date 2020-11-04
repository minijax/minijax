package org.minijax.rs.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

class PersistenceUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, PersistenceUtils::new);
    }

    @Test
    void testNotFound() {
        assertEquals(Collections.emptyList(), PersistenceUtils.getNames("not-found.xml"));
    }

    @Test
    void testNotXml() {
        assertEquals(Collections.emptyList(), PersistenceUtils.getNames("not-xml.txt"));
    }

    @Test
    void testOne() {
        assertEquals(Collections.singletonList("testdb"), PersistenceUtils.getNames("test-persistence1.xml"));
    }

    @Test
    void testMultiple() {
        assertEquals(Arrays.asList("test1", "test2"), PersistenceUtils.getNames("test-persistence2.xml"));
    }
}
