package org.minijax.nio;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.minijax.Minijax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Worker implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
    private final Minijax minijax;
    private final Selector selector;
    private final BufferPool bufferPool;
    private final Queue<SocketChannel> incoming;
    private final List<Connection> connections;
    private volatile boolean running;
    private Worker nextWorker;

    public Worker(final Minijax minijax, final SelectorProvider selectorProvider) throws IOException {
        this.minijax = minijax;
        this.selector = selectorProvider.openSelector();
        bufferPool = new BufferPool();
        incoming = new ArrayBlockingQueue<>(1000);
        connections = new ArrayList<>();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public Worker getNextWorker() {
        return nextWorker;
    }

    public void setNextWorker(final Worker nextWorker) {
        if (this.nextWorker != null) {
            throw new IllegalStateException("nextWorker should only be set once");
        }
        this.nextWorker = nextWorker;
    }

    public void accept(final SocketChannel socketChannel) {
        incoming.add(socketChannel);
        selector.wakeup();
    }

    @Override
    public void run() {
        LOG.debug("Starting worker...");
        running = true;

        try {
            while (running) {
                // For any incoming connections
                // remove from the queue
                // and register for reading
                SocketChannel incomingChannel;
                while ((incomingChannel = incoming.poll()) != null) {
                    LOG.debug("Worker processing {}", incomingChannel);
                    incomingChannel.configureBlocking(false);

                    final Socket socket = incomingChannel.socket();
                    socket.setSoTimeout(Config.READ_TIMEOUT);
                    socket.setTcpNoDelay(true);
                    socket.setReceiveBufferSize(8 * 1024);
                    socket.setSendBufferSize(8 * 1024);
                    socket.setReuseAddress(true);

                    final Connection conn = new Connection(minijax, incomingChannel, bufferPool.take());
                    connections.add(conn);
                    incomingChannel.register(selector, SelectionKey.OP_READ, conn);
                }

                if (selector.select(Config.SELECT_TIMEOUT) > 0) {
                    final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    final Iterator<?> iter = selectedKeys.iterator();
                    while (iter.hasNext()) {
                        final SelectionKey key = (SelectionKey) iter.next();
                        iter.remove();

                        final SocketChannel channel = (SocketChannel) key.channel();
                        final Connection conn = (Connection) key.attachment();
                        final boolean success = conn.handle();
                        if (!success) {
                            channel.close();
                            bufferPool.give(conn.getBuffer());
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            LOG.warn("Exception in listener thread: {}", ex.getMessage(), ex);
        }

        LOG.debug("Worker done.");
    }
}
