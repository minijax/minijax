package org.minijax;

import static org.junit.Assert.*;

import org.eclipse.jetty.server.Server;
import org.junit.Test;

public class ServerTest {

    public static class MockServer extends Server {
        public boolean started;
        public boolean joined;

        @Override
        protected void doStart() {
            started = true;
        }

        @Override
        public void join() {
            joined = true;
        }
    }


    @Test
    public void testRun() {
        final MockServer server = new MockServer();

        final Minijax minijax = new Minijax() {
            @Override
            protected Server createServer(final int port) {
                return server;
            }
        };

        minijax.run(8080);

        assertTrue(server.started);
        assertTrue(server.joined);
    }
}
