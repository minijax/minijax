package org.minijax.entity;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.minijax.entity.test.Widget;
import org.minijax.util.IdUtils;

public class BaseEntityTest {

    @Test
    public void testHashCode() {
        final Widget w1 = new Widget();
        assertEquals(0, w1.hashCode());

        final Widget w2 = new Widget(IdUtils.create());
        assertEquals(w2.getId().hashCode(), w2.hashCode());
    }


    @Test
    public void testEquals() {
        final Widget w1 = new Widget(IdUtils.create());
        final Widget w2 = new Widget(IdUtils.create());
        final Widget w3 = new Widget(w2.getId());

        assertNotEquals(w1, null);
        assertNotEquals(w1, new Object());
        assertNotEquals(w1, w2);

        assertEquals(w1, w1);
        assertEquals(w2, w3);
    }


    @Test
    public void testToJson() throws IOException {
        final Widget w = new Widget();
        w.setCreatedDateTime(ZonedDateTime.of(2017, 10, 30, 4, 38, 0, 0, ZoneId.of("America/Los_Angeles")).toInstant());
        w.setName("foo");
        assertEquals("{\"createdDateTime\":\"2017-10-30T11:38:00Z\",\"name\":\"foo\"}", w.toJson());
    }


    @Test
    public void testSqlHint() {
        final UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
        final Widget w = new Widget(id);
        assertEquals("SELECT * FROM `WIDGET` WHERE ID=UNHEX('00000000000000000000000000000000');", w.getSqlHint());
    }


    @Test
    public void testCopyProperties() {
        final Widget w1 = new Widget();
        w1.setHandle("foo");
        w1.setName("bar");

        final Widget w2 = new Widget();
        w2.copyNonNullProperties(w1);
        assertEquals("foo", w2.getHandle());
        assertEquals("bar", w2.getName());
    }


    @Test(expected = NullPointerException.class)
    public void testCopyPropertiesNull() {
        final Widget w = new Widget();
        w.copyNonNullProperties(null);
    }


    public static class DifferentEntity extends BaseEntity {
        private static final long serialVersionUID = 1L;
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCopyPropertiesDifferentClass() {
        final Widget w = new Widget();
        w.copyNonNullProperties(new DifferentEntity());
    }


    @Test
    public void testSortByCreatedDate() {
        final Widget w1 = new Widget(IdUtils.create());
        w1.setCreatedDateTime(Instant.now().minusSeconds(3L));

        final Widget w2 = new Widget(IdUtils.create());
        w2.setCreatedDateTime(Instant.now().minusSeconds(2L));

        final Widget w3 = new Widget(IdUtils.create());
        w3.setCreatedDateTime(Instant.now().minusSeconds(1L));

        final List<Widget> w = new ArrayList<>(Arrays.asList(w3, w2, w1));

        BaseEntity.sortByCreatedDateTime(w);
        assertEquals(w1, w.get(0));
        assertEquals(w2, w.get(1));
        assertEquals(w3, w.get(2));
    }
}
