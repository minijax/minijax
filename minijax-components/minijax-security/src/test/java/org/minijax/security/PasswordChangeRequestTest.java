package org.minijax.security;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PasswordChangeRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpPasswordChangeRequestTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testGettersSetters() {
        final User u = new User();

        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setUser(u);
        assertEquals(u.getId(), pcr.getUserId());
    }

    @Test
    public void testValidateSuccess() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("0123456789012345678901234567890123456789");
        pcr.setUser(new User());
        assertTrue(validator.validate(pcr).isEmpty());
    }

    @Test
    public void testValidateNullCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode(null);
        assertEquals(1, validator.validate(pcr).size());
    }

    @Test
    public void testValidateEmptyCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("");
        assertEquals(2, validator.validate(pcr).size());
    }

    @Test
    public void testValidateTooShortCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("01234567890123456789");
        assertEquals(2, validator.validate(pcr).size());
    }
}
