package org.minijax.cdi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.inject.InjectionException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Qualifier;
import jakarta.inject.Singleton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QualifierTest {
    private MinijaxInjector injector;

    @BeforeEach
    public void setUp() {
        injector = new MinijaxInjector();
    }

    @AfterEach
    public void tearDown() {
        injector.close();
    }

    @Singleton
    static class QualifiedSingleton {

    }

    static class QualifiedResource {
        @Inject
        @Named("a")
        QualifiedSingleton a;

        @Inject
        @Named("b")
        QualifiedSingleton b;
    }

    @Test
    public void testQualifiedSingleton() {
        final QualifiedResource r = injector.getResource(QualifiedResource.class);
        assertNotNull(r);
        assertNotNull(r.a);
        assertNotNull(r.b);
        assertNotEquals(r.a, r.b);
        assertNotSame(r.a, r.b);
    }

    @Qualifier
    @Retention(RUNTIME)
    @Target(FIELD)
    private @interface MyCustomQualifier {
    }

    private static class MultipleQualifiers {
        @Inject
        @Named("a")
        @MyCustomQualifier
        QualifiedSingleton a;
    }

    @Test
    public void testMultipleQualifiers() {
        assertThrows(InjectionException.class, () -> injector.getResource(MultipleQualifiers.class));
    }
}
