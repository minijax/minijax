package org.minijax.commons;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MinijaxPropertiesTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, MinijaxProperties::new);
    }
}
