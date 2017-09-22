package org.minijax.util;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new LocaleUtils();
    }

    @Test
    public void testNull() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage(null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testEmpty() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("");
        assertTrue(list.isEmpty());
    }

    @Test
    public void testWild() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("*");
        assertEquals(1, list.size());
        assertEquals("*", list.get(0).getLanguage());
    }

    @Test
    public void testMultiple() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("en, fr");
        assertEquals(2, list.size());
        assertEquals("en", list.get(0).getLanguage());
        assertEquals("fr", list.get(1).getLanguage());
    }

    @Test
    public void testMultipleWithCountry() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("en-US, fr");
        assertEquals(2, list.size());
        assertEquals("en", list.get(0).getLanguage());
        assertEquals("US", list.get(0).getCountry());
        assertEquals("fr", list.get(1).getLanguage());
    }

    @Test
    public void testVariant() {
        final List<Locale> list = LocaleUtils.parseAcceptLanguage("de-DE-1996");
        assertEquals(1, list.size());
        assertEquals("de", list.get(0).getLanguage());
        assertEquals("DE", list.get(0).getCountry());
        assertEquals("1996", list.get(0).getVariant());
    }
}
