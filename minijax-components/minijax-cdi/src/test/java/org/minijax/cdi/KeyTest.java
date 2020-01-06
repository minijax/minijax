package org.minijax.cdi;

import static org.junit.Assert.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.junit.Test;

public class KeyTest {

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
    public void testBasic() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            final Key<Object> k1 = injector.buildKey(Object.class);
            final Key<Object> k2 = injector.buildKey(Object.class);
            final Key<Exception> k3 = injector.buildKey(Exception.class);

            assertTrue(k1.equals(k1));
            assertTrue(k1.equals(k2));
            assertFalse(k1.equals(k3));
            assertEquals(k1.hashCode(), k2.hashCode());
            assertNotEquals(k1.hashCode(), k3.hashCode());

            assertFalse(k1.equals(null));
            assertFalse(k1.equals(new Object()));
        }
    }

    @Test
    public void testByName() {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            final Key<Object> k1 = injector.buildKey(Object.class, "MyName");
            final Key<Object> k2 = injector.buildKey(Object.class, "MyName");
            final Key<Object> k3 = injector.buildKey(Object.class, "OtherName");
            final Key<Object> k4 = injector.buildKey(Object.class);

            assertTrue(k1.equals(k2));
            assertFalse(k1.equals(k3));
            assertFalse(k1.equals(k4));
            assertEquals(k1.hashCode(), k2.hashCode());
            assertNotEquals(k1.hashCode(), k3.hashCode());
            assertNotEquals(k1.hashCode(), k4.hashCode());
        }
    }

    @Test
    public void testByQualifier() throws Exception {
        try (final MinijaxInjector injector = new MinijaxInjector()) {
            final Key<Object> k1 = injector.buildKey(Object.class, MyAnnotation1.class);
            final Key<Object> k2 = injector.buildKey(Object.class, KeyTest.class.getField("TEST_OBJECT").getAnnotations());
            final Key<Object> k3 = injector.buildKey(Object.class);

            assertTrue(k1.equals(k2));
            assertFalse(k1.equals(k3));
            assertEquals(k1.hashCode(), k2.hashCode());
            assertNotEquals(k1.hashCode(), k3.hashCode());
        }
    }
}
