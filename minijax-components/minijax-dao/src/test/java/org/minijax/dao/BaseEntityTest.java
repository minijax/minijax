package org.minijax.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class BaseEntityTest {

    @Test
    void testHashCode() {
        final Widget w2 = new Widget();
        assertEquals(w2.getId().hashCode(), w2.hashCode());
    }

    @Test
    void testEquals() {
        final Widget w1 = new Widget();
        final Widget w2 = new Widget();
        final Widget w3 = new Widget();
        w3.setId(w2.getId());

        assertNotEquals(w1, null);
        assertNotEquals(w1, new Object());
        assertNotEquals(w1, w2);

        assertEquals(w1, w1);
        assertEquals(w2, w3);
    }

    @Test
    void testToJson() throws IOException {
        final Widget w = new Widget();
        w.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        w.setCreatedDateTime(ZonedDateTime.of(2017, 10, 30, 4, 38, 0, 0, ZoneId.of("America/Los_Angeles")).toInstant());
        w.setName("foo");
        assertEquals(
                "{\"createdDateTime\":\"2017-10-30T11:38:00Z\",\"id\":\"00000000-0000-0000-0000-000000000000\",\"name\":\"foo\"}",
                w.toJson());
    }

    @Test
    void testCopyProperties() {
        final Widget w1 = new Widget();
        w1.setHandle("foo");
        w1.setName("bar");

        final Widget w2 = new Widget();
        w2.copyNonNullProperties(w1);
        assertEquals("foo", w2.getHandle());
        assertEquals("bar", w2.getName());
    }

    @Test
    void testCopyPropertiesNull() {
        assertThrows(NullPointerException.class, () -> {
            final Widget w = new Widget();
            w.copyNonNullProperties(null);
        });
    }

    static class DifferentEntity extends DefaultBaseEntity {
        private static final long serialVersionUID = 1L;
    }

    @Test
    void testCopyPropertiesDifferentClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            final Widget w = new Widget();
            w.copyNonNullProperties(new DifferentEntity());
        });
    }

    @Test
    void testSortByCreatedDate() {
        final Widget w1 = new Widget();
        w1.setCreatedDateTime(Instant.now().minusSeconds(3L));

        final Widget w2 = new Widget();
        w2.setCreatedDateTime(Instant.now().minusSeconds(2L));

        final Widget w3 = new Widget();
        w3.setCreatedDateTime(Instant.now().minusSeconds(1L));

        final List<Widget> w = new ArrayList<>(Arrays.asList(w3, w2, w1));

        BaseEntity.sortByCreatedDateTime(w);
        assertEquals(w1, w.get(0));
        assertEquals(w2, w.get(1));
        assertEquals(w3, w.get(2));
    }

    @Test
    void testFromJson() throws IOException {
        final String json = "{\"name\":\"foo\"}";
        final Widget w = Widget.fromJson(Widget.class, json);
        assertNotNull(w);
        assertEquals("foo", w.getName());
    }
}
