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

class NamedEntityTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpNamedEntityTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testCtor1() {
        final Widget w = new Widget();
        assertNotNull(w.getId());
        assertEquals("", w.getName());
    }

    @Test
    void testCtor2() {
        final Widget w = new Widget("foo");
        assertNotNull(w.getId());
        assertEquals("foo", w.getName());
    }

    @Test
    void testGettersSetters() {
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
    void testGenerateHandle() {
        final Widget w = new Widget();
        w.generateHandle();
        assertNotNull(w.getHandle());
    }

    @Test
    void testGenerateHandleFromName() {
        final Widget w = new Widget();
        w.setName("foo");
        w.generateHandle();
        assertNotNull(w.getHandle());
        assertTrue(w.getHandle().startsWith("foo"));
    }

    @Test
    void testValidate() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName("Foo");
        assertTrue(validator.validate(w).isEmpty());
    }

    @Test
    void testValidateNullHandle() {
        final Widget w = new Widget();
        w.setHandle(null);
        w.setName("foo");
        assertTrue(validator.validate(w).isEmpty());
    }

    @Test
    void testValidateEmptyHandle() {
        final Widget w = new Widget();
        w.setHandle("");
        w.setName("foo");
        assertEquals(2, validator.validate(w).size());
    }

    @Test
    void testValidateHandleTooLong() {
        final Widget w = new Widget();
        w.setHandle("x".repeat(40));
        w.setName("foo");
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    void testValidateHandleStartsWithPeriod() {
        final Widget w = new Widget();
        w.setHandle(".foo");
        w.setName("foo");
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    void testValidateHandleSpecialCharacters() {
        final Widget w = new Widget();
        w.setHandle("foo+");
        w.setName("foo");
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    void testValidateNullName() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName(null);
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    void testValidateEmptyName() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName("");
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    void testValidateNameTooLong() {
        final Widget w = new Widget();
        w.setHandle("foo");
        w.setName("x".repeat(300));
        assertEquals(1, validator.validate(w).size());
    }

    @Test
    void testSortByName() {
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
