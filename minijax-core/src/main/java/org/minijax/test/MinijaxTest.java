package org.minijax.test;

import java.net.URI;

import javax.ws.rs.client.WebTarget;

import org.minijax.Minijax;

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

    public void packages(final String... packageNames) {
        server.packages(packageNames);
    }

    public WebTarget target(final String uri) {
        return new MinijaxWebTarget(server, URI.create(uri));
    }
}
