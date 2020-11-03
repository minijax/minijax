package org.minijax.commons;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OptionalClassesTest {

    @Test
    public void testCtor() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new OptionalClasses();
        });
    }

    @Test
    public void testOptionalClasses() {

        // Persistence is available in tests
        assertNotNull(OptionalClasses.ENTITY_MANAGER);

        // Websockets are *not* available in tests
        assertNull(OptionalClasses.SERVER_ENDPOINT);
    }
}
