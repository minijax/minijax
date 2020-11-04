package com.example.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void testEquals() {
        final Owner o1 = new Owner();
        final Owner o2 = new Owner();
        final Owner o3 = new Owner();

        o2.setId(o1.getId());

        assertEquals(o1, o2);
        assertNotEquals(o1, null);
        assertNotEquals(o1, new Object());
        assertNotEquals(o1, o3);
    }

    @Test
    void testGettersSetters() {
        final Pet p = new Pet();

        final Owner o = new Owner();
        o.setName("Alice");
        o.setAddress("123 Lane");
        o.setCity("Smallville");
        o.setTelephone("555-5555");
        o.setPets(Collections.singletonList(p));

        assertEquals("Alice", o.getName());
        assertEquals("123 Lane", o.getAddress());
        assertEquals("Smallville", o.getCity());
        assertEquals("555-5555", o.getTelephone());
        assertEquals(Collections.singletonList(p), o.getPets());
    }
}
