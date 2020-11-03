package org.minijax.commons;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MinijaxPropertiesTest {

    @Test
    public void testCtor() {
        assertThrows(UnsupportedOperationException.class, MinijaxProperties::new);
    }
}
