package org.minijax.nio;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.minijax.Minijax;

public class ServerTest {

    @Test
    public void testInit() throws Exception {
        final Minijax minijax = new Minijax();
        final MinijaxNioServer server = new MinijaxNioServer(minijax);
        server.initSocket();
        assertNotNull(server.serverChannel);
        assertNotNull(server.serverSocket);
        assertNotNull(server.selector);
        assertTrue(server.running);
        server.close();
    }

    @Test
    public void testServer() throws Exception {
        final MockServerSocketChannel mockServerSocketChannel = new MockServerSocketChannel(null);
        final MockServerSocket mockServerSocket = new MockServerSocket();
        final MockSelector mockSelector = new MockSelector(null);

        final Minijax minijax = new Minijax();

        final MinijaxNioServer server = new MinijaxNioServer(minijax) {
            @Override
            void initSocket() throws IOException {
                this.serverChannel = mockServerSocketChannel;
                this.serverChannel.configureBlocking(false);
                this.serverSocket = mockServerSocket;
                this.selector = mockSelector;
                this.running = true;
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                server.running = false;
            }
        }, 1000);

        server.start();
        server.stop();
        server.close();

        assertEquals(1, mockSelector.select());
    }

}
