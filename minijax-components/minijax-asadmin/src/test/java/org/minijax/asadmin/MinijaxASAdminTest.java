package org.minijax.asadmin;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.asadmin.MinijaxASAdmin;
import org.minijax.rs.test.MinijaxTest;

public class MinijaxASAdminTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(MinijaxASAdmin.class);
    }

    @Test
    public void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }
}
