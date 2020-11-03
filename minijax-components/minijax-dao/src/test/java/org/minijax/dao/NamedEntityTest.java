package org.minijax.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.commons.IdUtils;

public class NamedEntityTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpNamedEntityTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testCtor1() {
        final Widget w = new Widget();
        assertNotNull(w.getId());
        assertEquals("", w.getName());
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
        assertTrue(validator.validate(w).isEmpty());
    }

    @Test
    public void testValidateNullHandle() {
        final Widget w = new Widget();
        w.setHandle(null);
        w.setName("foo");
        assertTrue(validator.validate(w).isEmpty());
    }

    @Test
    public void testValidateEmptyHandle() {
        final Widget w = new Widget();
        w.setHandle("");
        w.setName("foo");
        assertEquals(2, validator.validate(w).size());
    }

    @Test
    public void testValidateHandleTooLong() {
        final Widget w = new Widget();
        w.setHandle(repeat("x", 40));
        w.setName("foo");
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    public void testValidateHandleStartsWithPeriod() {
        final Widget w = new Widget();
        w.setHandle(".foo");
        w.setName("foo");
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    public void testValidateHandleSpecialCharacters() {
        final Widget w = new Widget();
        w.setHandle("foo+");
        w.setName("foo");
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    public void testValidateNullName() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName(null);
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    public void testValidateEmptyName() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName("");
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    public void testValidateNameTooLong() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName(repeat("x", 300));
        assertEquals(1, validator.validate(w).size());
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

    private static String repeat(final String str, final int times) {
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i < times; i++) {
            b.append(str);
        }
        return b.toString();
    }
}
