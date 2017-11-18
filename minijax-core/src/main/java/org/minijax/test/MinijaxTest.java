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
    public static void setUpMinijaxClass() {
        if (server == null) {
            startServer();
        }
    }

    @Before
    public void setUpMinijax() {
        if (server == null) {
            startServer();
        }
    }

    private static void startServer() {
        server = new Minijax();
    }

    private static void stopServer() {
        if (server != null) {
            server.getInjector().close();
            server = null;
        }
    }

    protected static void resetServer() {
        stopServer();
        startServer();
    }

    protected static Minijax getServer() {
        return server;
    }

    protected static void register(final Class<?> c) {
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

    protected static WebTarget target(final String uri) {
        return target(URI.create(uri));
    }

    protected static WebTarget target(final URI uri) {
        return new MinijaxWebTarget(server, uri);
    }

    protected static MinijaxRequestContext createRequestContext() {
        return createRequestContext("GET", "/");
    }

    private static MinijaxRequestContext createRequestContext(final String method, final String uri) {
        return new MinijaxRequestContext(
                getServer().getDefaultApplication(),
                new MockHttpServletRequest(method, URI.create(uri)),
                null);
    }
}
