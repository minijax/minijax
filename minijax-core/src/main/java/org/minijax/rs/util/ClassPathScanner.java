package org.minijax.rs.util;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

/**
 * Scans package names for classes to be automatically registered.
 *
 * Scanning implemented with the "ClassGraph" library:
 * https://github.com/classgraph/classgraph
 */
public class ClassPathScanner {
    private static final Logger LOG = LoggerFactory.getLogger(ClassPathScanner.class);
    private static final String[] AUTO_SCAN_CLASSES = {
            "jakarta.ws.rs.ApplicationPath",
            "jakarta.ws.rs.Path",
            "jakarta.ws.rs.ext.Provider",
            "jakarta.websocket.server.ServerEndpoint"
    };

    ClassPathScanner() {
        throw new UnsupportedOperationException();
    }

    public static Set<Class<?>> scan(final String... packageNames) {
        final Set<Class<?>> result = new HashSet<>();
        try (final ScanResult scanResult =
                new ClassGraph()
                    .enableClassInfo()
                    .enableAnnotationInfo()
                    .acceptPackages(packageNames)
                    .scan()) {

            for (final String annotation : AUTO_SCAN_CLASSES) {
                for (final ClassInfo classInfo : scanResult.getClassesWithAnnotation(annotation)) {
                    try {
                        result.add(Class.forName(classInfo.getName()));
                    } catch (final ClassNotFoundException | NoClassDefFoundError ex) {
                        LOG.error("Class error: {}", ex.getMessage(), ex);
                    }
                }
            }
        }
        return result;
    }
}
