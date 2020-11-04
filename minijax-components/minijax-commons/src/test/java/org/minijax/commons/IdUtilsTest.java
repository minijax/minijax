package org.minijax.commons;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class IdUtilsTest {

    @Test
    void testConstructor() {
        assertThrows(UnsupportedOperationException.class, IdUtils::new);
    }

    @Test
    void testCreate() {
        final UUID id = IdUtils.create();
        assertNotNull(id);
    }

    @Test
    void testParseNull() {
        assertNull(IdUtils.tryParse(null));
    }

    @Test
    void testParseEmpty() {
        assertNull(IdUtils.tryParse(""));
    }

    @Test
    void testParseBadCharacters() {
        assertNull(IdUtils.tryParse("!@#"));
    }

    @Test
    void testParseTooShort() {
        assertNull(IdUtils.tryParse("1234567890"));
    }

    @Test
    void testParseNormal() {
        final UUID id1 = IdUtils.create();
        final UUID id2 = IdUtils.tryParse(id1.toString());
        assertEquals(id1, id2);
    }

    @Test
    void testParseNoDashes() {
        final UUID id1 = IdUtils.create();
        final UUID id2 = IdUtils.tryParse(id1.toString().replaceAll("-", ""));
        assertEquals(id1, id2);
    }

    @Test
    void testToBytesNull() {
        assertNull(IdUtils.toBytes(null));
    }

    @Test
    void testToBytes() {
        final UUID id = IdUtils.create();
        final byte[] b1 = IdUtils.toBytes(id);
        final byte[] b2 = IdUtils.toBytes(id);
        assertNotNull(b1);
        assertEquals(16, b1.length);
        assertArrayEquals(b1, b2);
    }

    @Test
    void testFromBytesNull() {
        assertNull(IdUtils.fromBytes(null));
    }

    @Test
    void testFromBytesTooShort() {
        assertNull(IdUtils.fromBytes(new byte[] { 1, 2, 3 }));
    }

    @Test
    void testFromBytesTooLong() {
        assertNull(IdUtils.fromBytes(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 }));
    }
}
