package org.minijax.entity;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

import org.junit.Test;
import org.minijax.util.IdUtils;

public class UuidGeneratorTest {

    @Test
    public void testGetGeneratedValue() {
        final UuidGenerator g = new UuidGenerator();

        final byte[] bytes = g.getGeneratedValue(null, null, null);
        assertNotNull(bytes);

        final UUID id = IdUtils.fromBytes(bytes);
        assertNotNull(id);
    }


    @Test
    public void testGetGeneratedVector() {
        final UuidGenerator g = new UuidGenerator();

        final Vector<byte[]> bytes = g.getGeneratedVector(null, null, null, 100);
        assertNotNull(bytes);
        assertEquals(100, bytes.size());

        final Set<UUID> ids = bytes.stream().map(IdUtils::fromBytes).collect(Collectors.toSet());
        assertNotNull(ids);
        assertEquals(100, ids.size());
    }


    @Test
    public void testOverrides() {
        final UuidGenerator g = new UuidGenerator();
        g.onConnect();
        g.onDisconnect();
        assertFalse(g.shouldAcquireValueAfterInsert());
        assertFalse(g.shouldUsePreallocation());
        assertFalse(g.shouldUseTransaction());
    }


    @Test
    public void testEquals() {
        final UuidGenerator g1 = new UuidGenerator();
        final UuidGenerator g2 = new UuidGenerator();
        final UuidGenerator g3 = new UuidGenerator();
        g3.setName("other name");

        assertEquals(g1.hashCode(), g1.hashCode());
        assertEquals(g1.hashCode(), g2.hashCode());
        assertEquals(g1, g1);
        assertEquals(g1, g2);

        assertNotEquals(g1, null);
        assertNotEquals(g1, new Object());
        assertNotEquals(g1, g3);
    }
}
