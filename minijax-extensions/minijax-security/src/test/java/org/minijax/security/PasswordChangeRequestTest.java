/*
 * AJIBOT CONFIDENTIAL
 * __________________
 *
 *  2017 Ajibot Inc
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Ajibot Inc and its suppliers, if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Ajibot Inc and its suppliers and may
 * be covered by U.S. and Foreign Patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Ajibot Inc.
 */
package org.minijax.security;

import static org.junit.Assert.*;

import org.junit.Test;
import org.minijax.util.IdUtils;

public class PasswordChangeRequestTest {

    @Test
    public void testGettersSetters() {
        final User u = new User(IdUtils.create());

        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setUser(u);
        assertEquals(u, pcr.getUser());
    }


    @Test
    public void testValidateSuccess() {
        final PasswordChangeRequest pcr = new PasswordChangeRequest();
        pcr.setCode("0123456789012345678901234567890123456789");
        pcr.setUser(new User(IdUtils.create()));
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
