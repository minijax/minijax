package org.minijax.cdi;

import static org.junit.Assert.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.InjectionException;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;

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

    @PersistenceContext(unitName = "MyContext")
    public static final EntityManager TEST_ENTITY_MANAGER = null;

    @DefaultValue("foo")
    public String defaultValue1;

    @DefaultValue("bar")
    public String defaultValue2;

    @Context
    @MyAnnotation1
    public String tooManyStrategies;


    @Test
    public void testBasic() {
        final Key<Object> k1 = Key.of(Object.class);
        final Key<Object> k2 = Key.of(Object.class);
        final Key<Exception> k3 = Key.of(Exception.class);

        assertTrue(k1.equals(k1));
        assertTrue(k1.equals(k2));
        assertFalse(k1.equals(k3));
        assertEquals(k1.hashCode(), k2.hashCode());
        assertNotEquals(k1.hashCode(), k3.hashCode());

        assertFalse(k1.equals(null));
        assertFalse(k1.equals(new Object()));
    }


    @Test
    public void testByName() {
        final Key<Object> k1 = Key.of(Object.class, "MyName");
        final Key<Object> k2 = Key.of(Object.class, "MyName");
        final Key<Object> k3 = Key.of(Object.class, "OtherName");
        final Key<Object> k4 = Key.of(Object.class);

        assertTrue(k1.equals(k2));
        assertFalse(k1.equals(k3));
        assertFalse(k1.equals(k4));
        assertEquals(k1.hashCode(), k2.hashCode());
        assertNotEquals(k1.hashCode(), k3.hashCode());
        assertNotEquals(k1.hashCode(), k4.hashCode());
    }


    @Test
    public void testByQualifier() throws Exception {
        final Key<Object> k1 = Key.of(Object.class, MyAnnotation1.class);
        final Key<Object> k2 = Key.of(Object.class, KeyTest.class.getField("TEST_OBJECT").getAnnotations());
        final Key<Object> k3 = Key.of(Object.class);

        assertTrue(k1.equals(k2));
        assertFalse(k1.equals(k3));
        assertEquals(k1.hashCode(), k2.hashCode());
        assertNotEquals(k1.hashCode(), k3.hashCode());
    }


    @Test
    public void testDefaultValues() throws Exception {
        final Key<String> k1 = Key.of(String.class, KeyTest.class.getField("defaultValue1").getAnnotations());
        final Key<String> k2 = Key.of(String.class, KeyTest.class.getField("defaultValue1").getAnnotations());
        final Key<String> k3 = Key.of(String.class, KeyTest.class.getField("defaultValue2").getAnnotations());

        assertTrue(k1.equals(k2));
        assertFalse(k1.equals(k3));
        assertEquals(k1.hashCode(), k2.hashCode());
        assertNotEquals(k1.hashCode(), k3.hashCode());
    }


    @Test
    public void testByPersistenceContext() throws Exception {
        final Key<EntityManager> k1 = Key.ofPersistenceContext("MyContext");
        final Key<EntityManager> k2 = Key.of(EntityManager.class, KeyTest.class.getField("TEST_ENTITY_MANAGER").getAnnotations());
        final Key<EntityManager> k3 = Key.of(EntityManager.class);

        assertTrue(k1.equals(k2));
        assertFalse(k1.equals(k3));
        assertEquals(k1.hashCode(), k2.hashCode());
        assertNotEquals(k1.hashCode(), k3.hashCode());
    }


    @Test(expected = InjectionException.class)
    public void testTooManyStrategies() throws Exception {
        Key.of(EntityManager.class, KeyTest.class.getField("tooManyStrategies").getAnnotations());
    }
}
