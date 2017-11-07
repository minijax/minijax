package org.minijax.util;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

public class IdUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new IdUtils();
    }


    @Test
    public void testCreate() {
        final UUID id = IdUtils.create();
        assertNotNull(id);
    }


    @Test
    public void testParseNull() {
        assertNull(IdUtils.tryParse(null));
    }


    @Test
    public void testParseEmpty() {
        assertNull(IdUtils.tryParse(""));
    }


    @Test
    public void testParseBadCharacters() {
        assertNull(IdUtils.tryParse("!@#"));
    }


    @Test
    public void testParseTooShort() {
        assertNull(IdUtils.tryParse("1234567890"));
    }


    @Test
    public void testParseNormal() {
        final UUID id1 = IdUtils.create();
        final UUID id2 = IdUtils.tryParse(id1.toString());
        assertEquals(id1, id2);
    }


    @Test
    public void testParseNoDashes() {
        final UUID id1 = IdUtils.create();
        final UUID id2 = IdUtils.tryParse(id1.toString().replaceAll("-", ""));
        assertEquals(id1, id2);
    }

    @Test
    public void testToBytesNull() {
        assertNull(IdUtils.toBytes(null));
    }

    @Test
    public void testToBytes() {
        final UUID id = IdUtils.create();
        final byte[] b1 = IdUtils.toBytes(id);
        final byte[] b2 = IdUtils.toBytes(id);
        assertNotNull(b1);
        assertEquals(b1.length, 16);
        assertArrayEquals(b1, b2);
    }

    @Test
    public void testFromBytesNull() {
        assertNull(IdUtils.fromBytes(null));
    }

    @Test
    public void testFromBytesTooShort() {
        assertNull(IdUtils.fromBytes(new byte[] { 1, 2, 3 }));
    }

    @Test
    public void testFromBytesTooLong() {
        assertNull(IdUtils.fromBytes(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 }));
    }
}
