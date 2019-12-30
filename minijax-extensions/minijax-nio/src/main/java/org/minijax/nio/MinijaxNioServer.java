package org.minijax.nio;

import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.minijax.Minijax;
import org.minijax.MinijaxServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxNioServer implements MinijaxServer {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxNioServer.class);
    final ExecutorService executorService;
    final Listener listener;
    final Worker[] workers;

    public MinijaxNioServer(final Minijax minijax, final SelectorProvider selectorProvider) throws IOException, InterruptedException {
        final int threadCount = Runtime.getRuntime().availableProcessors() * 2;
        final int workerCount = threadCount - 1;

        LOG.info("Thread count: {}", threadCount);
        executorService = Executors.newFixedThreadPool(threadCount);

        // Create the listener.
        // The listeners job is only to listen on the server socket for incoming connections.
        listener = new Listener(selectorProvider);

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
        for (int i = 0; i < workers.length; i++) {
            executorService.execute(workers[i]);
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
        for (int i = 0; i < workers.length; i++) {
            workers[i].setRunning(false);
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
    }
}
