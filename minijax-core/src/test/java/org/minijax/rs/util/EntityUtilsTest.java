package org.minijax.rs.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Entity;

import org.junit.Test;
import org.minijax.Minijax;
import org.minijax.rs.MinijaxException;
import org.minijax.rs.util.EntityUtils;

public class EntityUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new EntityUtils();
    }

    @Test(expected = MinijaxException.class)
    public void testReadUnknownType() throws IOException {
        final InputStream input = new ByteArrayInputStream("hello".getBytes());
        EntityUtils.readEntity(Minijax.class, null, null, null, null, input);
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
