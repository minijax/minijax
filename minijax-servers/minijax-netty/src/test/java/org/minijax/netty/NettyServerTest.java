package org.minijax.netty;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

class NettyServerTest {

    @Test
    void testStartStop() throws Exception {
        final Minijax minijax = new Minijax();
        final MinijaxNettyServer server = new MinijaxNettyServer(minijax);
        server.start();
        assertNotNull(server.getChannel());
        server.stop();
    }
}
