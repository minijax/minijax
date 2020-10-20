package org.minijax.rs.delegates;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;

import jakarta.ws.rs.core.GenericType;

import org.junit.Test;
import org.minijax.rs.Widget;
import org.minijax.rs.WidgetWriter;
import org.minijax.rs.test.MinijaxTest;

public class ResponseTest extends MinijaxTest {

    @Test
    public void testNullEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertNull(r.getEntity());
        assertNull(r.getEntityClass());
        assertNull(r.readEntity(String.class));
    }

    @Test
    public void testStringEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().entity("Hello").build();
        assertEquals("Hello", r.getEntity());
        assertEquals(String.class, r.getEntityClass());
        assertEquals("Hello", r.readEntity(String.class));
    }

    @Test
    public void testInputStreamEntity() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello".getBytes(StandardCharsets.UTF_8));

        final MinijaxResponse r = new MinijaxResponseBuilder().entity(inputStream).build();
        assertEquals(inputStream, r.getEntity());
        assertEquals(ByteArrayInputStream.class, r.getEntityClass());
        assertEquals("Hello", r.readEntity(String.class));
    }

    @Test
    public void testWidgetEntity() throws IOException {
        register(WidgetWriter.class);

        final Widget widget = new Widget();
        widget.setId("123");
        widget.setValue("Hello");

        final MinijaxResponse r = new MinijaxResponseBuilder(getServer().getDefaultApplication()).entity(widget).build();
        assertEquals(widget, r.getEntity());
        assertEquals(Widget.class, r.getEntityClass());
        assertEquals("(widget 123 Hello)", r.readEntity(String.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBufferEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.bufferEntity();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllowedMethods() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getAllowedMethods();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityAnnotations() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getEntityAnnotations();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityStream() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getEntityStream();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEntityType() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getEntityType();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLink() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getLink(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLinkBuilder() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getLinkBuilder(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMetadata() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getMetadata();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetStringHeaders() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.getStringHeaders();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testhasEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.hasEntity();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testhasLink() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.hasLink(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity2() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.readEntity((Class<?>) null, (Annotation[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity3() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.readEntity((GenericType<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadEntity4() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.readEntity((GenericType<?>) null, (Annotation[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetEntityStream() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        r.setEntityStream(null);
    }
}
