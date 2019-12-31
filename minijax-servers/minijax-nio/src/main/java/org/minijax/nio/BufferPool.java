package org.minijax.nio;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Pool of ByteBuffer instances.
 *
 * This class is not thread safe.  One pool should be used per worker.
 */
class BufferPool {
    private static final int BUFFER_SIZE = 8 * 1024;
    private final Queue<ByteBuffer> queue;

    public BufferPool() {
        queue = new ArrayDeque<>();
    }

    public ByteBuffer take() {
        if (queue.isEmpty()) {
            return ByteBuffer.allocateDirect(BUFFER_SIZE);
        }
        return queue.poll();
    }

    public void give(final ByteBuffer buffer) {
        queue.offer(buffer);
    }
}
