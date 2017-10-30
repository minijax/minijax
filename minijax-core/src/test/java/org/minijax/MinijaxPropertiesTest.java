package org.minijax;

import org.junit.Test;
import org.minijax.MinijaxProperties;

public class MinijaxPropertiesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MinijaxProperties();
    }
}
