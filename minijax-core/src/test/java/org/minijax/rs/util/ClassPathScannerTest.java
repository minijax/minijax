package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;

import org.junit.jupiter.api.Test;

class ClassPathScannerTest {

    @Test
    void testScanner() throws IOException {
        final Set<Class<?>> result = ClassPathScanner.scan("org.minijax");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testJarScanner() throws IOException {
        final URLClassLoader loader = new URLClassLoader(new URL[] { new URL("jar:file:src/test/resources/dummy.jar!/") });
        final Set<Class<?>> result = ClassPathScanner.scan("com.example", loader);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testUnknownProtocol() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            final URL url = new URL("http://test");
            final Vector<URL> urlList = new Vector<>();
            urlList.add(url);

            final ClassLoader mockLoader = new ClassLoader() {
                @Override
                public Enumeration<URL> getResources(final String name) throws IOException {
                    return urlList.elements();
                }
            };

            ClassPathScanner.scan("com.example", mockLoader);
        });
    }
}
