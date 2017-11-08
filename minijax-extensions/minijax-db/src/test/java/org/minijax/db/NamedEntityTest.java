package org.minijax.db;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.minijax.db.Avatar;
import org.minijax.db.NamedEntity;
import org.minijax.db.test.Widget;
import org.minijax.util.IdUtils;

public class NamedEntityTest {

    @Test
    public void testCtor1() {
        final Widget w = new Widget();
        assertNotNull(w.getId());
        assertNull(w.getName());
    }

    @Test
    public void testCtor2() {
        final Widget w = new Widget("foo");
        assertNotNull(w.getId());
        assertEquals("foo", w.getName());
    }

    @Test
    public void testGettersSetters() {
        final Widget w = new Widget();
        w.setId(IdUtils.create());
        w.setCreatedDateTime(Instant.now());
        w.setUpdatedDateTime(Instant.now().plusSeconds(1L));
        w.setName("foo");
        w.setHandle("bar");
        w.setAvatar(new Avatar("http://img1", "http://img2"));
        w.setDeleted(false);

        assertEquals(w.getId(), w.getId());
        assertEquals(w.getCreatedDateTime(), w.getCreatedDateTime());
        assertEquals(w.getUpdatedDateTime(), w.getUpdatedDateTime());
        assertEquals("foo", w.getName());
        assertEquals("bar", w.getHandle());
        assertEquals("http://img1", w.getAvatar().getImageUrl());
        assertEquals("http://img2", w.getAvatar().getThumbUrl());
        assertEquals("/widgets/" + w.getId(), w.getUri().toString());
        assertFalse(w.isDeleted());
        assertNull(w.getDeletedDateTime());

        w.setDeleted(true);
        assertTrue(w.isDeleted());
        assertNotNull(w.getDeletedDateTime());
    }

    @Test
    public void testGenerateHandle() {
        final Widget w = new Widget();
        w.generateHandle();
        assertNotNull(w.getHandle());
    }

    @Test
    public void testGenerateHandleFromName() {
        final Widget w = new Widget();
        w.setName("foo");
        w.generateHandle();
        assertNotNull(w.getHandle());
        assertTrue(w.getHandle().startsWith("foo"));
    }

    @Test
    public void testValidate() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName("Foo");
        w.validate();
    }

    @Test
    public void testValidateNullHandle() {
        final Widget w = new Widget();
        w.setHandle(null);
        w.setName("foo");
        w.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmptyHandle() {
        final Widget w = new Widget();
        w.setHandle("");
        w.setName("foo");
        w.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateHandleTooLong() {
        final Widget w = new Widget();
        w.setHandle(StringUtils.repeat("x", 40));
        w.setName("foo");
        w.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateHandleStartsWithPeriod() {
        final Widget w = new Widget();
        w.setHandle(".foo");
        w.setName("foo");
        w.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateHandleSpecialCharacters() {
        final Widget w = new Widget();
        w.setHandle("foo+");
        w.setName("foo");
        w.validate();
    }

    @Test(expected = NullPointerException.class)
    public void testValidateNullName() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName(null);
        w.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmptyName() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName("");
        w.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateNameTooLong() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName(StringUtils.repeat("x", 300));
        w.validate();
    }

    @Test
    public void testSortByName() {
        final List<Widget> w = new ArrayList<>(Arrays.asList(
                new Widget("Carol"),
                new Widget("Bob"),
                new Widget("Alice")));

        NamedEntity.sortByName(w);
        assertEquals("Alice", w.get(0).getName());
        assertEquals("Bob", w.get(1).getName());
        assertEquals("Carol", w.get(2).getName());
    }
}
