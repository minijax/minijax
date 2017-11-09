package org.minijax.websocket;

import org.eclipse.jetty.server.Server;

class MockServer extends Server {
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
