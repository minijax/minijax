package org.minijax.nio;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConfigTest {

    @Test
    public void testCtor() {
        assertThrows(UnsupportedOperationException.class, Config::new);
    }
}
