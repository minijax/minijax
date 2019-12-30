package org.minijax.nio;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Test;

public class BufferPoolTest {

    @Test
    public void testPool() {
        final BufferPool pool = new BufferPool();

        final ByteBuffer buf1 = pool.take();
        assertNotNull(buf1);

        final ByteBuffer buf2 = pool.take();
        assertNotNull(buf2);
        assertFalse(buf1 == buf2);

        pool.give(buf1);
        final ByteBuffer buf3 = pool.take();
        assertNotNull(buf3);
        assertTrue(buf1 == buf3);
    }
}
