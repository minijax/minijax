package org.minijax.security;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.minijax.util.IdUtils;

public class ApiKeyTest {

    @Test
    public void testGettersSetters() {
        final User user = new User(IdUtils.create());

        final UUID id = IdUtils.create();

        final ApiKey e = new ApiKey();
        e.setId(id);
        e.setName("bar");
        e.setUser(user);

        assertEquals(id, e.getId());
        assertEquals("bar", e.getName());
        assertEquals(user, e.getUser());
    }


    @Test
    public void testHashCode() {
        final UUID id = IdUtils.create();
        final ApiKey msg = new ApiKey(id);
        assertEquals(id.hashCode(), msg.hashCode());
    }


    @Test
    public void testEquals() {
        final UUID id1 = IdUtils.create();
        final UUID id2 = IdUtils.create();

        final ApiKey m1 = new ApiKey(id1);
        final ApiKey m2 = new ApiKey(id1);
        final ApiKey m3 = new ApiKey(id2);

        assertTrue(m1.equals(m1));
        assertTrue(m1.equals(m2));
        assertFalse(m1.equals(m3));
        assertFalse(m1.equals(new Object()));
        assertFalse(m1.equals(null));
    }


    @Test
    public void testToJson() throws IOException {
        final UUID id = IdUtils.create();

        final ApiKey m = new ApiKey(id);
        final String json = m.toJson();

        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"" + id + "\""));
    }
}
