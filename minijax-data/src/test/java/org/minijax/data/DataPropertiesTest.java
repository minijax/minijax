package org.minijax.data;

import org.junit.Test;

public class DataPropertiesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new DataProperties();
    }
}
