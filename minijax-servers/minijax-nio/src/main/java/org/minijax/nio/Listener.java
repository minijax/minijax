package org.minijax.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Listener implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);
    private final SelectorProvider selectorProvider;
    private final int port;
    private volatile boolean running;
    private Worker currentWorker;

    public Listener(final SelectorProvider selectorProvider, final int port) {
        this.selectorProvider = selectorProvider;
        this.port = port;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public void setCurrentWorker(final Worker currentWorker) {
        this.currentWorker = currentWorker;
    }

    @Override
    public void run() {
        LOG.info("Starting listener...");
        running = true;

        LOG.info("Creating server channel...");
        try (final ServerSocketChannel serverChannel = selectorProvider.openServerSocketChannel()) {
            serverChannel.configureBlocking(false);

            LOG.info("Creating server socket...");
            final ServerSocket serverSocket = serverChannel.socket();
            serverSocket.setReceiveBufferSize(Config.LISTENER_BUFFER_SIZE);
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port), Config.MAX_CONNECTIONS);

            LOG.info("Registering server...");
            final Selector selector = selectorProvider.openSelector();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (running) {
                if (selector.select(Config.SELECT_TIMEOUT) == 0) {
                    continue;
                }

                final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                final Iterator<?> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    iter.next();
                    iter.remove();

                    final SocketChannel channel = serverChannel.accept();
                    LOG.debug("Listener accepted {}", channel);
                    currentWorker.accept(channel);
                    currentWorker = currentWorker.getNextWorker();
                }
            }
        } catch (final IOException ex) {
            LOG.warn("Exception in listener thread: {}", ex.getMessage(), ex);
        }

        LOG.info("Listener done.");
    }
}
