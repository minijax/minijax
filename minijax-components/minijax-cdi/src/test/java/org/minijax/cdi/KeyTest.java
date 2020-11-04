package org.minijax.cdi;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.inject.Qualifier;

import org.junit.jupiter.api.Test;

class KeyTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Qualifier
    public @interface MyAnnotation1 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Qualifier
    public @interface MyAnnotation2 {
    }

    @MyAnnotation1
    public static final Object TEST_OBJECT = new Object();

    @Test
    void testBasic() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            final Key<Object> k1 = injector.buildKey(Object.class);
            final Key<Object> k2 = injector.buildKey(Object.class);
            final Key<Exception> k3 = injector.buildKey(Exception.class);

            assertEquals(k1, k2);
            assertNotEquals(k1, k3);
            assertEquals(k1.hashCode(), k2.hashCode());
            assertNotEquals(k1.hashCode(), k3.hashCode());
            assertNotEquals(k1, null);
            assertNotEquals(k1, new Object());
        }
    }

    @Test
    void testByName() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            final Key<Object> k1 = injector.buildKey(Object.class, "MyName");
            final Key<Object> k2 = injector.buildKey(Object.class, "MyName");
            final Key<Object> k3 = injector.buildKey(Object.class, "OtherName");
            final Key<Object> k4 = injector.buildKey(Object.class);

            assertEquals(k1, k2);
            assertNotEquals(k1, k3);
            assertNotEquals(k1, k4);
            assertEquals(k1.hashCode(), k2.hashCode());
            assertNotEquals(k1.hashCode(), k3.hashCode());
            assertNotEquals(k1.hashCode(), k4.hashCode());
        }
    }

    @Test
    void testByQualifier() throws Exception {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            final Key<Object> k1 = injector.buildKey(Object.class, MyAnnotation1.class);
            final Key<Object> k2 = injector.buildKey(Object.class, KeyTest.class.getField("TEST_OBJECT").getAnnotations());
            final Key<Object> k3 = injector.buildKey(Object.class);

            assertEquals(k1, k2);
            assertNotEquals(k1, k3);
            assertEquals(k1.hashCode(), k2.hashCode());
            assertNotEquals(k1.hashCode(), k3.hashCode());
        }
    }
}
