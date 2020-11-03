package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.ws.rs.client.Entity;

import org.junit.jupiter.api.Test;
import org.minijax.Minijax;
import org.minijax.commons.MinijaxException;

public class EntityUtilsTest {

    @Test
    public void testCtor() {
        assertThrows(UnsupportedOperationException.class, EntityUtils::new);
    }

    @Test
    public void testReadUnknownType() throws IOException {
        assertThrows(MinijaxException.class, () -> {
        final InputStream input = new ByteArrayInputStream("hello".getBytes());
        EntityUtils.readEntity(Minijax.class, null, null, null, null, input);
    });
    }

    @Test
    public void testReadInputStream() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[1]);
        final InputStream result = EntityUtils.readEntity(InputStream.class, null, null, null, null, input);
        assertEquals(input, result);
    }

    @Test
    public void testReadCustomType() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[1]);
        final InputStream result = EntityUtils.readEntity(InputStream.class, null, null, null, null, input);
        assertEquals(input, result);
    }

    @Test
    public void testReadString() throws IOException {
        final InputStream input = new ByteArrayInputStream("hello".getBytes());
        final String result = EntityUtils.readEntity(String.class, null, null, null, null, input);
        assertEquals("hello", result);
    }

    @Test
    public void testWriteNull() throws IOException {
        assertNull(EntityUtils.writeEntity(null, null));
        assertNull(EntityUtils.writeEntity(Entity.entity(null, "text/plain"), null));
    }

    @Test
    public void testWriteInputStream() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[1]);
        assertEquals(input, EntityUtils.writeEntity(Entity.entity(input, "text/plain"), null));
    }
}
