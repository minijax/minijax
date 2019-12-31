package org.minijax.security;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minijax.util.IdUtils;

public class UserSessionTest {
    private static Validator validator;

    @BeforeClass
    public static void setUpUserSessionTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

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
        assertTrue(validator.validate(s).isEmpty());
    }


    @Test
    public void testValidateRequireUser() {
        final UserSession s = new UserSession();
        assertEquals(1, validator.validate(s).size());
    }
}
