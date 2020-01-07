package org.minijax.nio;

import org.junit.Test;

public class ConfigTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new Config();
    }
}
