package org.minijax.rs.util;

import static org.junit.Assert.*;

import java.io.Closeable;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;
import org.minijax.rs.util.CloseUtils;

public class CloseUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new CloseUtils();
    }

    @Test
    @SuppressWarnings("squid:S2699")
    public void testCloseNull() {
        CloseUtils.closeQuietly((Object) null);
    }

    @Test
    public void testAutoCloseable() {
        final MyAutoCloseable obj = new MyAutoCloseable();
        CloseUtils.closeQuietly(obj);
        assertTrue(obj.closed);
    }

    @Test
    @SuppressWarnings("squid:S2699")
    public void testExplodingAutoCloseable() {
        final ExplodingAutoCloseable obj = new ExplodingAutoCloseable();
        CloseUtils.closeQuietly(obj);
    }

    @Test
    public void testCloseable() {
        final MyCloseable obj = new MyCloseable();
        CloseUtils.closeQuietly(obj);
        assertTrue(obj.closed);
    }

    @Test
    public void testEntityManager() {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("testdb");
        assertNotNull(emf);
        assertTrue(emf.isOpen());

        final EntityManager em = emf.createEntityManager();
        assertNotNull(em);
        assertTrue(em.isOpen());

        CloseUtils.closeQuietly(em);
        assertFalse(em.isOpen());

        CloseUtils.closeQuietly(emf);
        assertFalse(emf.isOpen());
    }

    /*
     * Helper classes
     */

    public static class MyAutoCloseable implements AutoCloseable {
        boolean closed;
        @Override
        public void close() {
            closed = true;
        }
    }

    public static class ExplodingAutoCloseable implements AutoCloseable {
        @Override
        public void close() throws IOException {
            throw new IOException("boom");
        }
    }

    public static class MyCloseable implements Closeable {
        boolean closed;
        @Override
        public void close() {
            closed = true;
        }
    }
}
