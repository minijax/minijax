package org.minijax;

import static org.junit.Assert.*;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.junit.Test;

public class ServerTest {

    public static class MockServer extends Server {
        public Handler handler;
        public boolean started;
        public boolean joined;

        @Override
        public void setHandler(final Handler handler) {
            this.handler = handler;
        }

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

        assertNotNull(server.handler);
        assertTrue(server.started);
        assertTrue(server.joined);
    }


    @Test
    public void testStaticFile() {
        final MockServer server = new MockServer();

        final Minijax minijax = new Minijax() {
            @Override
            protected Server createServer(final int port) {
                return server;
            }
        };

        minijax.addStaticFile("static/hello.txt").run(8080);

        assertNotNull(server.handler);
        assertTrue(server.started);
        assertTrue(server.joined);
    }


    @Test
    public void testStaticDirectory() {
        final MockServer server = new MockServer();

        final Minijax minijax = new Minijax() {
            @Override
            protected Server createServer(final int port) {
                return server;
            }
        };

        minijax.addStaticDirectory("static").run(8080);

        assertNotNull(server.handler);
        assertTrue(server.started);
        assertTrue(server.joined);
    }
}
