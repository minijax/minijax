package org.minijax.security;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PasswordChangeRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpPasswordChangeRequestTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testGettersSetters() {
        final User u = new User();

        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setUser(u);
        assertEquals(u.getId(), pcr.getUserId());
    }

    @Test
    void testValidateSuccess() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("0123456789012345678901234567890123456789");
        pcr.setUser(new User());
        assertTrue(validator.validate(pcr).isEmpty());
    }

    @Test
    void testValidateNullCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode(null);
        assertEquals(1, validator.validate(pcr).size());
    }

    @Test
    void testValidateEmptyCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("");
        assertEquals(2, validator.validate(pcr).size());
    }

    @Test
    void testValidateTooShortCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("01234567890123456789");
        assertEquals(2, validator.validate(pcr).size());
    }
}
