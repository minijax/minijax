package org.minijax.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class IOUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new IOUtils();
    }

    @Test
    public void testToByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        final byte[] output = IOUtils.toByteArray(input);
        assertEquals(4, output.length);
    }

    @Test
    public void testToInputStream() throws IOException {
        final InputStream input = IOUtils.toInputStream("test", StandardCharsets.UTF_8);
        assertEquals("test", IOUtils.toString(input, StandardCharsets.UTF_8));
    }
}
