package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;
import org.minijax.rs.test.MinijaxTestWebTarget;

class SingletonTest {

    interface Counter {
        int getCount();
    }

    @Singleton
    public static class SingletonCounter implements Counter {
        private int count;

        @Override
        public int getCount() {
            return ++count;
        }
    }

    static class SingletonResource {
        @Inject
        private SingletonCounter counter;

        @GET
        @Path("/singleton")
        public int get() {
            return counter.getCount();
        }
    }

    static class CounterResource {
        @Inject
        private Counter counter;

        @GET
        @Path("/counter")
        public int get() {
            return counter.getCount();
        }
    }

    @Test
    void testDirectClassMatch() {
        final Minijax server = new Minijax()
                .register(SingletonResource.class);

        assertEquals(1, getCount(server, "/singleton"));
        assertEquals(2, getCount(server, "/singleton"));
        assertEquals(3, getCount(server, "/singleton"));
    }

    @Test
    void testBindSingleton() {
        final Minijax server = new Minijax()
                .bind(new SingletonCounter(), Counter.class)
                .register(CounterResource.class, CounterResource.class);

        assertEquals(1, getCount(server, "/counter"));
        assertEquals(2, getCount(server, "/counter"));
        assertEquals(3, getCount(server, "/counter"));
    }

    private static int getCount(final Minijax server, final String path) {
        return new MinijaxTestWebTarget(server).path(path).request().get(Integer.class);
    }
}
