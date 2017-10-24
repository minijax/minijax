package com.example.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;
import org.minijax.util.IdUtils;

public class OwnerTest {

    @Test
    public void testEquals() {
        final UUID id = IdUtils.create();

        final Owner o1 = new Owner(id, "Alice", "123 Lane", "Smallville", "555-5555");
        final Owner o2 = new Owner(id, "Alice", "123 Lane", "Smallville", "555-5555");
        final Owner o3 = new Owner(id, "Frank", "123 Lane", "Smallville", "555-5555");
        final Owner o4 = new Owner(id, "Alice", "456 Lane", "Smallville", "555-5555");
        final Owner o5 = new Owner(id, "Alice", "123 Lane", "Weirdville", "555-5555");
        final Owner o6 = new Owner(id, "Alice", "123 Lane", "Smallville", "666-6666");

        assertEquals(o1, o2);
        assertNotEquals(o1, null);
        assertNotEquals(o1, new Object());
        assertNotEquals(o1, o3);
        assertNotEquals(o1, o4);
        assertNotEquals(o1, o5);
        assertNotEquals(o1, o6);
    }

    @Test
    public void testGettersSetters() {
        final Pet p = new Pet();

        final Owner o = new Owner();
        o.setName("Alice");
        o.setAddress("123 Lane");
        o.setCity("Smallville");
        o.setTelephone("555-5555");
        o.setPets(Arrays.asList(p));

        assertEquals("Alice", o.getName());
        assertEquals("123 Lane", o.getAddress());
        assertEquals("Smallville", o.getCity());
        assertEquals("555-5555", o.getTelephone());
        assertEquals(Arrays.asList(p), o.getPets());
    }
}
