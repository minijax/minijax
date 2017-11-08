package org.minijax.test;

import java.net.URI;

import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.BeforeClass;
import org.minijax.Minijax;
import org.minijax.MinijaxRequestContext;

public class MinijaxTest {
    private static Minijax server;

    @BeforeClass
    public static void setUpClass() {
        if (server == null) {
            startServer();
        }
    }

    @Before
    public void setUp() {
        if (server == null) {
            startServer();
        }
    }

    public static void startServer() {
        server = new Minijax();
    }

    public static void stopServer() {
        if (server != null) {
            server.getInjector().close();
            server = null;
        }
    }

    public static void resetServer() {
        stopServer();
        startServer();
    }

    public static Minijax getServer() {
        return server;
    }

    public static void register(final Class<?> c) {
        server.register(c);
    }

    public static void register(final Object component) {
        server.register(component);
    }

    public static void register(final Object component, final Class<?>... contracts) {
        server.register(component, contracts);
    }

    public static void packages(final String... packageNames) {
        server.packages(packageNames);
    }

    public static WebTarget target(final String uri) {
        return new MinijaxWebTarget(server, URI.create(uri));
    }

    protected static MinijaxRequestContext createRequestContext() {
        return createRequestContext("GET", "/");
    }

    protected static MinijaxRequestContext createRequestContext(final String method, final String uri) {
        return new MinijaxRequestContext(
                getServer().getDefaultApplication(),
                new MockHttpServletRequest(method, URI.create(uri)),
                null);
    }
}
