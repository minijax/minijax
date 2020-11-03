package org.minijax.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.minijax.commons.IdUtils;

public class UserSessionTest {
    private static Validator validator;

    @BeforeAll
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
