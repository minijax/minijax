package org.minijax.rs;

import org.junit.Test;
import org.minijax.rs.MinijaxProperties;

public class MinijaxPropertiesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new MinijaxProperties();
    }
}
