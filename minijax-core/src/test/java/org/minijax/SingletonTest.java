package org.minijax;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.junit.Test;
import org.minijax.test.MinijaxWebTarget;

public class SingletonTest {

    public static interface Counter {
        public int getCount();
    }

    @Singleton
    public static class SingletonCounter implements Counter {
        private int count;

        @Override
        public int getCount() {
            return ++count;
        }
    }


    public static class SingletonResource {
        @Inject
        private SingletonCounter counter;

        @GET
        @Path("/singleton")
        public int get() {
            return counter.getCount();
        }
    }


    public static class CounterResource {
        @Inject
        private Counter counter;

        @GET
        @Path("/counter")
        public int get() {
            return counter.getCount();
        }
    }


    @Test
    public void testRegisterClass() {
        final Minijax server = new Minijax()
                .register(SingletonCounter.class)
                .register(SingletonResource.class);

        assertEquals(1, getCount(server, "/singleton"));
        assertEquals(2, getCount(server, "/singleton"));
        assertEquals(3, getCount(server, "/singleton"));
    }


    @Test
    public void testRegisterInstance() {
        final Minijax server = new Minijax()
                .register(new SingletonCounter())
                .register(SingletonResource.class);

        assertEquals(1, getCount(server, "/singleton"));
        assertEquals(2, getCount(server, "/singleton"));
        assertEquals(3, getCount(server, "/singleton"));
    }


    @Test
    public void testRegisterInstanceWithContract() {
        final Minijax server = new Minijax()
                .register(new SingletonCounter(), Counter.class)
                .register(CounterResource.class, CounterResource.class);

        assertEquals(1, getCount(server, "/counter"));
        assertEquals(2, getCount(server, "/counter"));
        assertEquals(3, getCount(server, "/counter"));
    }


    @Test
    public void testRegisterInstanceWithIgnoredPriority() {
        final Minijax server = new Minijax()
                .register(new SingletonCounter(), 1)
                .register(SingletonResource.class, 1);

        assertEquals(1, getCount(server, "/singleton"));
        assertEquals(2, getCount(server, "/singleton"));
        assertEquals(3, getCount(server, "/singleton"));
    }


    @Test
    public void testRegisterInstanceWithContractMap() {
        final Map<Class<?>, Integer> counterContract = new HashMap<>();
        counterContract.put(Counter.class, 1);

        final Map<Class<?>, Integer> resourceContract = new HashMap<>();
        resourceContract.put(CounterResource.class, 1);

        final Minijax server = new Minijax()
                .register(new SingletonCounter(), counterContract)
                .register(CounterResource.class, resourceContract);

        assertEquals(1, getCount(server, "/counter"));
        assertEquals(2, getCount(server, "/counter"));
        assertEquals(3, getCount(server, "/counter"));
    }


    private static int getCount(final Minijax server, final String path) {
        return new MinijaxWebTarget(server, URI.create(path)).request().get(Integer.class);
    }
}
