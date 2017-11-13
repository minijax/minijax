package org.minijax.security;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordChangeRequestTest {

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
        pcr.validate();
    }


    @Test(expected = NullPointerException.class)
    public void testValidateNullCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode(null);
        pcr.validate();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmptyCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("");
        pcr.validate();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testValidateTooShortCode() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("01234567890123456789");
        pcr.validate();
    }
}
