package org.minijax.asadmin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.minijax.rs.test.MinijaxTest;

class MinijaxASAdminTest extends MinijaxTest {

    @Before
    public void setUp() {
        register(MinijaxASAdmin.class);
    }

    @Test
    void testHello() {
        assertEquals("Hello world!", target("/").request().get(String.class));
    }
}
