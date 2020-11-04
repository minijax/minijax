package org.minijax.nio;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void testCtor() {
        assertThrows(UnsupportedOperationException.class, Config::new);
    }
}
