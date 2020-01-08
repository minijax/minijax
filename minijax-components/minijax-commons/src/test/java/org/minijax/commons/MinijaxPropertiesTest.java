package org.minijax.commons;

import org.junit.Test;
import org.minijax.commons.MinijaxProperties;

public class MinijaxPropertiesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MinijaxProperties();
    }
}
