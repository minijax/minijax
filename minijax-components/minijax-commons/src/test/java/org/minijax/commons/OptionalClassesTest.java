package org.minijax.commons;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OptionalClassesTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, OptionalClasses::new);
    }

    @Test
    void testOptionalClasses() {

        // Persistence is available in tests
        assertNotNull(OptionalClasses.ENTITY_MANAGER);

        // Websockets are *not* available in tests
        assertNull(OptionalClasses.SERVER_ENDPOINT);
    }
}
