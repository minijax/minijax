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
