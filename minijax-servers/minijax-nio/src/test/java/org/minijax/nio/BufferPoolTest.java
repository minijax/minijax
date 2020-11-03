package org.minijax.nio;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

public class BufferPoolTest {

    @Test
    public void testPool() {
        final BufferPool pool = new BufferPool();

        final ByteBuffer buf1 = pool.take();
        assertNotNull(buf1);

        final ByteBuffer buf2 = pool.take();
        assertNotNull(buf2);
        assertNotSame(buf1, buf2);

        pool.give(buf1);
        final ByteBuffer buf3 = pool.take();
        assertNotNull(buf3);
        assertSame(buf1, buf3);
    }
}
