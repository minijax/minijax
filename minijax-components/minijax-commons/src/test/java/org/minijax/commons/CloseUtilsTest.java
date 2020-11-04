package org.minijax.commons;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Test;

class CloseUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, CloseUtils::new);
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testCloseNull() {
        CloseUtils.closeQuietly((Object) null);
    }

    @Test
    void testAutoCloseable() {
        final MyAutoCloseable obj = new MyAutoCloseable();
        CloseUtils.closeQuietly(obj);
        assertTrue(obj.closed);
    }

    @Test
    @SuppressWarnings("resource")
    void testAutoCloseableCollection() {
        final List<MyAutoCloseable> list = Arrays.asList(new MyAutoCloseable(), new MyAutoCloseable());
        CloseUtils.closeQuietly(list);
        assertTrue(list.get(0).closed);
        assertTrue(list.get(1).closed);
    }

    @Test
    @SuppressWarnings("squid:S2699")
    void testExplodingAutoCloseable() {
        final ExplodingAutoCloseable obj = new ExplodingAutoCloseable();
        CloseUtils.closeQuietly(obj);
    }

    @Test
    void testCloseable() {
        final MyCloseable obj = new MyCloseable();
        CloseUtils.closeQuietly(obj);
        assertTrue(obj.closed);
    }

    @Test
    void testEntityManagerFactoryOpen() {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        when(emf.isOpen()).thenReturn(true);
        CloseUtils.closeQuietly(emf);
        verify(emf, times(1)).isOpen();
        verify(emf, times(1)).close();
    }

    @Test
    void testEntityManagerFactoryClosed() {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        when(emf.isOpen()).thenReturn(false);
        CloseUtils.closeQuietly(emf);
        verify(emf, times(1)).isOpen();
        verify(emf, never()).close();
    }

    @Test
    void testEntityManagerOpen() {
        final EntityManager em = mock(EntityManager.class);
        when(em.isOpen()).thenReturn(true);
        CloseUtils.closeQuietly(em);
        verify(em, times(1)).isOpen();
        verify(em, times(1)).close();
    }

    @Test
    void testEntityManagerClosed() {
        final EntityManager em = mock(EntityManager.class);
        when(em.isOpen()).thenReturn(false);
        CloseUtils.closeQuietly(em);
        verify(em, times(1)).isOpen();
        verify(em, never()).close();
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
