package org.minijax.nio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

class ServerTest {

    @Test
    void testServer() throws Exception {
        final Minijax minijax = new Minijax();
        final MinijaxNioServer server = new MinijaxNioServer(minijax, new MockSelectorProvider());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                server.stop();
            }
        }, 500);

        server.start();
        assertNotNull(server);
        assertNotNull(server.executorService);
        assertNotNull(server.listener);
        assertNotNull(server.workers);
        server.executorService.awaitTermination(1000, TimeUnit.SECONDS);
    }
}
