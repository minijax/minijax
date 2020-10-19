package org.minijax.commons;

import org.junit.Test;

public class MinijaxPropertiesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MinijaxProperties();
    }
}
