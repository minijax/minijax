package org.minijax.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class AvatarTest {

    @Test
    public void testGettersSetters() {
        final Avatar a = new Avatar();
        a.setImageType(Avatar.IMAGE_TYPE_MANUAL);
        a.setImageUrl("http://foo");
        a.setThumbUrl("http://bar");

        assertEquals(Avatar.IMAGE_TYPE_MANUAL, a.getImageType());
        assertEquals("http://foo", a.getImageUrl());
        assertEquals("http://bar", a.getThumbUrl());
    }

    @Test
    public void testHelperCtor() {
        final Avatar a = new Avatar("http://foo", "http://bar");
        assertEquals("http://foo", a.getImageUrl());
        assertEquals("http://bar", a.getThumbUrl());
    }
}
