package org.minijax.commons;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class IOUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, IOUtils::new);
    }

    @Test
    void testToByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        final byte[] output = IOUtils.toByteArray(input);
        assertEquals(4, output.length);
    }

    @Test
    void testToInputStream() throws IOException {
        final InputStream input = IOUtils.toInputStream("test", StandardCharsets.UTF_8);
        assertEquals("test", IOUtils.toString(input, StandardCharsets.UTF_8));
    }
}
