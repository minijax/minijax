package org.minijax.nio;

import org.junit.Test;

public class DateHeaderTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new DateHeader();
    }
}
