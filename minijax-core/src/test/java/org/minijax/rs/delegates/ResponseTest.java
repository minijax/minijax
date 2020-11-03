package org.minijax.rs.delegates;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;

import jakarta.ws.rs.core.GenericType;

import org.junit.jupiter.api.Test;
import org.minijax.rs.MinijaxRequestContext;
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

        try (final MinijaxRequestContext context = createRequestContext()) {
            final MinijaxResponse r = new MinijaxResponseBuilder(context).entity(widget).build();
            assertEquals(widget, r.getEntity());
            assertEquals(Widget.class, r.getEntityClass());
            assertEquals("(widget 123 Hello)", r.readEntity(String.class));
        }
    }

    @Test
    public void testBufferEntity() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.bufferEntity();
        });
    }

    @Test
    public void testGetAllowedMethods() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.getAllowedMethods();
        });
    }

    @Test
    public void testGetEntityAnnotations() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.getEntityAnnotations();
        });
    }

    @Test
    public void testGetEntityStream() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.getEntityStream();
        });
    }

    @Test
    public void testGetEntityType() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.getEntityType();
        });
    }

    @Test
    public void testGetLink() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.getLink(null);
        });
    }

    @Test
    public void testGetLinkBuilder() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.getLinkBuilder(null);
        });
    }

    @Test
    public void testGetMetadata() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.getMetadata();
        });
    }

    @Test
    public void testGetStringHeaders() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.getStringHeaders();
        });
    }

    @Test
    public void testhasEntity() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.hasEntity();
        });
    }

    @Test
    public void testhasLink() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.hasLink(null);
        });
    }

    @Test
    public void testReadEntity2() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.readEntity((Class<?>) null, (Annotation[]) null);
        });
    }

    @Test
    public void testReadEntity3() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.readEntity((GenericType<?>) null);
        });
    }

    @Test
    public void testReadEntity4() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.readEntity((GenericType<?>) null, (Annotation[]) null);
        });
    }

    @Test
    public void testSetEntityStream() {
        assertThrows(UnsupportedOperationException.class, () -> {
            final MinijaxResponse r = new MinijaxResponseBuilder().build();
            r.setEntityStream(null);
        });
    }
}
