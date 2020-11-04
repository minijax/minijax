package org.minijax.nio;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;

class WorkerTest {

    @Test
    void testEmptyRun() throws IOException {
        final Minijax minijax = new Minijax();
        final Worker worker = new Worker(minijax, new MockSelectorProvider());
        assertFalse(worker.isRunning());
        schedule(() -> worker.setRunning(false), 100);
        worker.run();
    }

    @Test
    void testAcceptHttp10() throws IOException {
        final Minijax minijax = new Minijax();
        final MockSelectorProvider selectorProvider = new MockSelectorProvider();
        final MockSocketChannel channel = new MockSocketChannel(selectorProvider, "GET / HTTP/1.0\r\n");
        final Worker worker = new Worker(minijax, selectorProvider);
        assertFalse(worker.isRunning());
        worker.accept(channel);
        schedule(() -> worker.setRunning(false), 100);
        worker.run();
    }

    @Test
    void testAcceptHttp11() throws IOException {
        final Minijax minijax = new Minijax();
        final MockSelectorProvider selectorProvider = new MockSelectorProvider();
        final MockSocketChannel channel = new MockSocketChannel(selectorProvider, "GET / HTTP/1.1\r\n");
        final Worker worker = new Worker(minijax, selectorProvider);
        assertFalse(worker.isRunning());
        worker.accept(channel);
        schedule(() -> worker.setRunning(false), 100);
        worker.run();
    }

    @Test
    void testSetNextWorker() throws IOException {
        assertThrows(IllegalStateException.class, () -> {
        final Worker worker = new Worker(null, new MockSelectorProvider());
        worker.setNextWorker(worker);
        worker.setNextWorker(worker);
    });
    }

    private static void schedule(final Runnable task, final int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, delay);
    }
}
