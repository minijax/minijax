package org.minijax.rs.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class LocaleUtilsTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, LocaleUtils::new);
    }

    @Test
    void testNull() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage(null);
        assertTrue(list.isEmpty());
    }

    @Test
    void testEmpty() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("");
        assertTrue(list.isEmpty());
    }

    @Test
    void testWild() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("*");
        assertEquals(1, list.size());
        assertEquals("*", list.get(0).getLanguage());
    }

    @Test
    void testMultiple() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("en, fr");
        assertEquals(2, list.size());
        assertEquals("en", list.get(0).getLanguage());
        assertEquals("fr", list.get(1).getLanguage());
    }

    @Test
    void testMultipleWithCountry() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("en-US, fr");
        assertEquals(2, list.size());
        assertEquals("en", list.get(0).getLanguage());
        assertEquals("US", list.get(0).getCountry());
        assertEquals("fr", list.get(1).getLanguage());
    }

    @Test
    void testVariant() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("de-DE-1996");
        assertEquals(1, list.size());
        assertEquals("de", list.get(0).getLanguage());
        assertEquals("DE", list.get(0).getCountry());
        assertEquals("1996", list.get(0).getVariant());
    }
}
