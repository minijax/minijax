package org.minijax.test;

import java.net.URI;

import javax.ws.rs.client.WebTarget;

import org.minijax.Minijax;
import org.minijax.MinijaxRequestContext;

public class MinijaxTest {
    private final Minijax server;

    public MinijaxTest() {
        server = new Minijax();
    }

    public Minijax getServer() {
        return server;
    }

    public void register(final Class<?> c) {
        server.register(c);
    }

    public void register(final Object component) {
        server.register(component);
    }

    public void register(final Object component, final Class<?>... contracts) {
        server.register(component, contracts);
    }

    public void packages(final String... packageNames) {
        server.packages(packageNames);
    }

    public WebTarget target(final String uri) {
        return new MinijaxWebTarget(server, URI.create(uri));
    }

    protected MinijaxRequestContext createRequestContext() {
        return createRequestContext("GET", "/");
    }

    protected MinijaxRequestContext createRequestContext(final String method, final String uri) {
        return new MinijaxRequestContext(
                getServer().getDefaultApplication(),
                new MockHttpServletRequest(method, URI.create(uri)),
                null);
    }
}
