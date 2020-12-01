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

class ResponseTest extends MinijaxTest {

    @Test
    void testNullEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertNull(r.getEntity());
        assertNull(r.getEntityClass());
        assertNull(r.readEntity(String.class));
    }

    @Test
    void testStringEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().entity("Hello").build();
        assertEquals("Hello", r.getEntity());
        assertEquals(String.class, r.getEntityClass());
        assertEquals("Hello", r.readEntity(String.class));
    }

    @Test
    void testInputStreamEntity() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello".getBytes(StandardCharsets.UTF_8));

        final MinijaxResponse r = new MinijaxResponseBuilder().entity(inputStream).build();
        assertEquals(inputStream, r.getEntity());
        assertEquals(ByteArrayInputStream.class, r.getEntityClass());
        assertEquals("Hello", r.readEntity(String.class));
    }

    @Test
    void testWidgetEntity() throws IOException {
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
    void testGetHeaderString() {
        final MinijaxResponse r = new MinijaxResponseBuilder()
                .header("b", "")
                .header("c", "foo")
                .header("d", "foo")
                .header("d", "bar")
                .build();
        assertEquals(null, r.getHeaderString("a"));
        assertEquals("", r.getHeaderString("b"));
        assertEquals("foo", r.getHeaderString("c"));
        assertEquals("foo,bar", r.getHeaderString("d"));
    }

    @Test
    void testBufferEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.bufferEntity();
        });
    }

    @Test
    void testGetAllowedMethods() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.getAllowedMethods();
        });
    }

    @Test
    void testGetEntityAnnotations() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.getEntityAnnotations();
        });
    }

    @Test
    void testGetEntityStream() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.getEntityStream();
        });
    }

    @Test
    void testGetEntityType() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.getEntityType();
        });
    }

    @Test
    void testGetLink() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.getLink(null);
        });
    }

    @Test
    void testGetLinkBuilder() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.getLinkBuilder(null);
        });
    }

    @Test
    void testGetMetadata() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.getMetadata();
        });
    }

    @Test
    void testGetStringHeaders() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.getStringHeaders();
        });
    }

    @Test
    void testhasEntity() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.hasEntity();
        });
    }

    @Test
    void testhasLink() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.hasLink(null);
        });
    }

    @Test
    void testReadEntity2() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.readEntity((Class<?>) null, (Annotation[]) null);
        });
    }

    @Test
    void testReadEntity3() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.readEntity((GenericType<?>) null);
        });
    }

    @Test
    void testReadEntity4() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.readEntity((GenericType<?>) null, (Annotation[]) null);
        });
    }

    @Test
    void testSetEntityStream() {
        final MinijaxResponse r = new MinijaxResponseBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> {
            r.setEntityStream(null);
        });
    }
}
