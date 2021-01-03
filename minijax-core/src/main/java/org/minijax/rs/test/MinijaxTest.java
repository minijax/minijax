package org.minijax.rs.test;

import static jakarta.ws.rs.HttpMethod.*;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.minijax.Minijax;
import org.minijax.rs.MinijaxRequestContext;

public class MinijaxTest {
    private static Minijax server;

    @BeforeAll
    public static void setUpMinijaxClass() {
        if (server == null) {
            startServer();
        }
    }

    @BeforeEach
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

    protected static void bind(final Object instance, final Class<?> contract) {
        server.bind(instance, contract);
    }

    protected static void bind(final Class<?> component, final Class<?> contract) {
        server.bind(component, contract);
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

    protected URI getBaseUri() {
        return URI.create("/");
    }

    protected static MinijaxTestClient client() {
        return new MinijaxTestClient(server);
    }

    protected static MinijaxTestWebTarget target(final String uri) {
        return client().target(uri);
    }

    protected static MinijaxTestWebTarget target(final URI uri) {
        return client().target(uri);
    }

    protected static MinijaxRequestContext createRequestContext() {
        return createRequestContext(GET, "/");
    }

    protected static MinijaxRequestContext createRequestContext(final String method, final String uri) {
        return new MinijaxTestRequestContext(
                getServer().getDefaultApplication(),
                method,
                uri);
    }
}
