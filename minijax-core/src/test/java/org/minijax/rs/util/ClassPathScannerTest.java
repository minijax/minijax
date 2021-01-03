package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

class ClassPathScannerTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, ClassPathScanner::new);
    }

    @Test
    void testScanner() {
        final Set<Class<?>> result = ClassPathScanner.scan("org.minijax");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
