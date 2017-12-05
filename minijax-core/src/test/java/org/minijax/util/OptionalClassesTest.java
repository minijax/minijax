package org.minijax.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionalClassesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new OptionalClasses();
    }

    @Test
    public void testOptionalClasses() {

        // Persistence is available in tests
        assertNotNull(OptionalClasses.ENTITY_MANAGER);

        // Websockets are *not* available in tests
        assertNull(OptionalClasses.SERVER_ENDPOINT);
    }
}
