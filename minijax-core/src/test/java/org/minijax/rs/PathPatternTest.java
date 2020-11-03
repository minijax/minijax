package org.minijax.rs;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.ws.rs.PathParam;

import org.junit.jupiter.api.Test;
import org.minijax.commons.IdUtils;

public class PathPatternTest {

    public static void get1() {
    }

    public static void get2(@PathParam("x") final String x) {
    }

    public static void get3(@PathParam("x") final String x, @PathParam("y") final String y) {
    }

    public static void getInt(@PathParam("x") final int x) {
    }

    public static void getLong(@PathParam("x") final long x) {
    }

    public static void getShort(@PathParam("x") final short x) {
    }

    public static void getDouble(@PathParam("x") final double x) {
    }

    public static void getFloat(@PathParam("x") final float x) {
    }

    public static void getUuid(@PathParam("x") final UUID x) {
    }

    @Test
    public void testGetPathParams() {
        assertNull(MinijaxPathPattern.parse(getMethod("get1"), "").getParams());
        assertNull(MinijaxPathPattern.parse(getMethod("get1"), "/").getParams());
        assertNull(MinijaxPathPattern.parse(getMethod("get1"), "/foo").getParams());
        assertEquals("x", MinijaxPathPattern.parse(getMethod("get2"), "/{x}").getParams().get(0));
        assertEquals("x", MinijaxPathPattern.parse(getMethod("get2"), "/foo/{x}").getParams().get(0));
        assertEquals("x", MinijaxPathPattern.parse(getMethod("get2"), "/foo/{x:[A-Z]+}").getParams().get(0));
        assertEquals("y", MinijaxPathPattern.parse(getMethod("get3"), "/foo/{x}/bar/{y}").getParams().get(1));
    }

    @Test
    public void testConvertPathToRegex() {
        assertEquals("/", MinijaxPathPattern.parse(getMethod("get1"), "/").getPatternString());
        assertEquals("/x", MinijaxPathPattern.parse(getMethod("get1"), "/x").getPatternString());
        assertEquals("/x/y", MinijaxPathPattern.parse(getMethod("get1"), "/x/y").getPatternString());
        assertEquals("/(?<x>[^/]+)", MinijaxPathPattern.parse(getMethod("get2"), "/{x}").getPatternString());
        assertEquals("/(?<x>[0-9]+)", MinijaxPathPattern.parse(getMethod("get2"), "/{x:[0-9]+}").getPatternString());
        assertEquals("/(?<x>\\d+)", MinijaxPathPattern.parse(getMethod("get2"), "/{x:\\d+}").getPatternString());
    }

    @Test
    public void testDefaultRegex() {
        assertEquals("/(?<x>-?[0-9]+)", MinijaxPathPattern.parse(getMethod("getInt"), "/{x}").getPatternString());
        assertEquals("/(?<x>-?[0-9]+)", MinijaxPathPattern.parse(getMethod("getLong"), "/{x}").getPatternString());
        assertEquals("/(?<x>-?[0-9]+)", MinijaxPathPattern.parse(getMethod("getShort"), "/{x}").getPatternString());

        assertEquals(
                MinijaxPathPattern.parse(getMethod("getDouble"), "/{x}").getPatternString(),
                MinijaxPathPattern.parse(getMethod("getFloat"), "/{x}").getPatternString());

        assertEquals("/(?<x>[0-9a-f]{8}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{12})", MinijaxPathPattern.parse(getMethod("getUuid"), "/{x}").getPatternString());
    }

    @Test
    public void testIntRegex() {
        final Pattern p = MinijaxPathPattern.parse(getMethod("getInt"), "/{x}").getPattern();
        assertTrue(p.matcher("/100").matches());
        assertTrue(p.matcher("/0").matches());
        assertTrue(p.matcher("/-1").matches());
        assertTrue(p.matcher("/1000000").matches());
        assertTrue(p.matcher("/" + Integer.MAX_VALUE).matches());
        assertFalse(p.matcher("/").matches());
        assertFalse(p.matcher("/x").matches());
    }

    @Test
    public void testDoubleRegex() {
        final Pattern p = MinijaxPathPattern.parse(getMethod("getDouble"), "/{x}").getPattern();
        assertTrue(p.matcher("/100").matches());
        assertTrue(p.matcher("/0").matches());
        assertTrue(p.matcher("/-1").matches());
        assertTrue(p.matcher("/1000000").matches());
        assertTrue(p.matcher("/" + Integer.MAX_VALUE).matches());
        assertTrue(p.matcher("/0.0").matches());
        assertTrue(p.matcher("/1.0").matches());
        assertTrue(p.matcher("/-1.0").matches());
        assertTrue(p.matcher("/1.").matches());
        assertTrue(p.matcher("/.1").matches());
        assertFalse(p.matcher("/").matches());
        assertFalse(p.matcher("/x").matches());
    }

    @Test
    public void testUuidRegex() {
        final Pattern p = MinijaxPathPattern.parse(getMethod("getUuid"), "/{x}").getPattern();
        assertTrue(p.matcher("/" + UUID.randomUUID().toString()).matches());
        assertTrue(p.matcher("/" + IdUtils.create().toString()).matches());
        assertTrue(p.matcher("/00000000-0000-0000-0000-000000000000").matches());
        assertTrue(p.matcher("/00000000000000000000000000000000").matches());
        assertFalse(p.matcher("/").matches());
        assertFalse(p.matcher("/x").matches());
        assertFalse(p.matcher("/00000000-0000-0000-0000-00000000000x").matches());
    }

    @Test
    public void testUnexpectedCurly() {
        assertThrows(IllegalArgumentException.class, () -> MinijaxPathPattern.parse(getMethod("get1"), "}"));
    }

    @Test
    public void testUnexpectedEnd() {
        assertThrows(IllegalArgumentException.class, () -> MinijaxPathPattern.parse(getMethod("get1"), "{"));
    }

    @Test
    public void testMissingName() {
        assertThrows(IllegalArgumentException.class, () -> MinijaxPathPattern.parse(getMethod("get1"), "{}"));
    }

    @Test
    public void testMissingNameWithWhitespace() {
        assertThrows(IllegalArgumentException.class, () -> MinijaxPathPattern.parse(getMethod("get1"), "{ : }"));
    }

    @Test
    public void testMissingParam() {
        assertThrows(IllegalArgumentException.class, () -> MinijaxPathPattern.parse(getMethod("get1"), "{test}"));
    }

    @Test
    public void testUnexpectedColon() {
        assertThrows(IllegalArgumentException.class, () -> MinijaxPathPattern.parse(getMethod("get1"), "{:}"));
    }

    @Test
    public void testNestedCurly() {
        assertEquals("/(?<x>[a-z]{3})", MinijaxPathPattern.parse(getMethod("get2"), "/{x:[a-z]{3}}").getPatternString());
    }

    private static Method getMethod(final String methodName) {
        for (final Method m : PathPatternTest.class.getMethods()) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }
}
