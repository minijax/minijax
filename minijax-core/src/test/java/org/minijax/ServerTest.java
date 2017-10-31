package org.minijax;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.Test;

public class ServerTest {

    public static class MockServer extends Server {
        public Connector[] connectors;
        public Handler handler;
        public boolean started;
        public boolean joined;

        @Override
        public void setConnectors(final Connector[] connectors) {
            this.connectors = connectors;
        }

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
            protected Server createServer() {
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
            protected Server createServer() {
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
            protected Server createServer() {
                return server;
            }
        };

        minijax.addStaticDirectory("static").run(8080);

        assertNotNull(server.handler);
        assertTrue(server.started);
        assertTrue(server.joined);
    }


    @Test
    public void testSsl() {
        final MockServer server = new MockServer();

        final Minijax minijax = new Minijax() {
            @Override
            protected Server createServer() {
                return server;
            }
        };

        minijax.property(MinijaxProperties.SSL_KEY_STORE_PATH, "keystore.jks")
                .property(MinijaxProperties.SSL_KEY_STORE_PASSWORD, "certpassword")
                .property(MinijaxProperties.SSL_KEY_MANAGER_PASSWORD, "certpassword")
                .run(8080);

        final Connector[] connectors = server.connectors;
        assertNotNull(connectors);
        assertEquals(1, connectors.length);

        final ServerConnector connector = (ServerConnector) connectors[0];
        assertNotNull(connector);

        final List<ConnectionFactory> factories = new ArrayList<>(connector.getConnectionFactories());
        assertEquals(2, factories.size());
        assertEquals("org.eclipse.jetty.server.SslConnectionFactory", factories.get(0).getClass().getName());
    }
}
