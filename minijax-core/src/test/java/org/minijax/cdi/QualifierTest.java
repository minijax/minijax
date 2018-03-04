package org.minijax.cdi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import static org.junit.Assert.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QualifierTest {
    private MinijaxInjector injector;

    @Before
    public void setUp() {
        injector = new MinijaxInjector();
    }

    @After
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
        assertTrue(r.a != r.b);
    }

    @Qualifier
    @Retention(RUNTIME)
    @Target(FIELD)
    private @interface MyCustomQualifier {}

    private static class MultipleQualifiers {
        @Inject
        @Named("a")
        @MyCustomQualifier
        QualifiedSingleton a;
    }

    @Test(expected = InjectException.class)
    public void testMultipleQualifiers() {
        injector.getResource(MultipleQualifiers.class);
    }
}
