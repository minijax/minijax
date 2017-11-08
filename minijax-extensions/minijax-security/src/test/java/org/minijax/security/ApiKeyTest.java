package org.minijax.security;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.minijax.util.IdUtils;

public class ApiKeyTest {

    @Test
    public void testGettersSetters() {
        final User user = new User();

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
    public void testEquals() {
        final ApiKey m1 = new ApiKey();
        final ApiKey m2 = new ApiKey();
        final ApiKey m3 = new ApiKey();

        m2.setId(m1.getId());

        assertTrue(m1.equals(m1));
        assertTrue(m1.equals(m2));
        assertFalse(m1.equals(m3));
        assertFalse(m1.equals(new Object()));
        assertFalse(m1.equals(null));
    }


    @Test
    public void testToJson() throws IOException {
        final ApiKey m = new ApiKey();
        final String json = m.toJson();

        assertNotNull(json);
        assertTrue(json.contains("\"id\":\"" + m.getId() + "\""));
    }
}
