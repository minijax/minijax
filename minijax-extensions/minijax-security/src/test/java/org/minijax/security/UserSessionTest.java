package org.minijax.security;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.minijax.util.IdUtils;

public class UserSessionTest {

    @Test
    public void testGettersSetters() {
        final UUID id = IdUtils.create();

        final User u = new User();

        final UserSession s = new UserSession();
        s.setId(id);
        s.setUser(u);

        assertEquals(id, s.getId());
        assertEquals(u, s.getUser());
        assertEquals(u.getId(), s.getUserId());
    }


    @Test
    public void testValidate() {
        final UserSession s = new UserSession();
        s.setUser(new User());
        s.validate();
    }


    @Test(expected = NullPointerException.class)
    public void testValidateRequireUser() {
        final UserSession s = new UserSession();
        s.validate();
    }
}
