package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ClassPathScannerTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, ClassPathScanner::new);
    }

    @Test
    void testScanner() throws IOException {
        final Set<Class<?>> result = ClassPathScanner.scan("org.minijax");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
