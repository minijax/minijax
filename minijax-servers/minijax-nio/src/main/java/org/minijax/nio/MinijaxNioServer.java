package org.minijax.nio;

import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.minijax.Minijax;
import org.minijax.rs.MinijaxServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxNioServer implements MinijaxServer {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxNioServer.class);
    final ExecutorService executorService;
    final Listener listener;
    final Worker[] workers;

    /**
     * Create a new server using the system default SelectorProvider.
     * @param minijax The minijax application.
     */
    public MinijaxNioServer(final Minijax minijax) throws IOException {
        this(minijax, SelectorProvider.provider());
    }

    /**
     * Create a new server using the specified SelectorProvider.
     * This is only used for tests.
     * @param minijax The minijax application.
     * @param selectorProvider The test selector provider.
     */
    MinijaxNioServer(final Minijax minijax, final SelectorProvider selectorProvider) throws IOException {
        final int threadCount = Runtime.getRuntime().availableProcessors() * 2;
        final int workerCount = threadCount - 2;

        LOG.info("Thread count: {}", threadCount);
        executorService = Executors.newFixedThreadPool(threadCount);

        // Start the Date header service
        DateHeader.start();

        // Create the listener.
        // The listeners job is only to listen on the server socket for incoming connections.
        listener = new Listener(selectorProvider, minijax.getPort());

        // Create the workers.
        // The workers process incoming connections and manage the reading and writing.
        workers = new Worker[workerCount];
        for (int i = 0; i < workerCount; i++) {
            workers[i] = new Worker(minijax, selectorProvider);
        }

        // Connect the workers.
        // Requests are handled round robin.  Create a circular linked list.
        for (int i = 0; i < workerCount; i++) {
            workers[i].setNextWorker(workers[(i + 1) % workerCount]);
        }

        // Set the listener's current worker.
        listener.setCurrentWorker(workers[0]);
    }

    @Override
    public void start() {
        // Add the workers to the executor
        for (Worker worker : workers) {
            executorService.execute(worker);
        }

        // Add the listener to the executor
        executorService.execute(listener);
    }

    @Override
    public void stop() {
        // Stop the listener
        LOG.info("Stopping listener...");
        listener.setRunning(false);

        // Stop all workers
        LOG.info("Stopping workers...");
        for (Worker worker : workers) {
            worker.setRunning(false);
        }

        // Shutdown the executor
        executorService.shutdown();

        // Wait for everything to complete
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            LOG.warn("Termination interrupted: {}", ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }

        // Stop the date service
        DateHeader.stop();
    }
}
