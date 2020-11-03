package org.minijax.nio;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DateHeaderTest {

    @Test
    public void testCtor() {
        assertThrows(UnsupportedOperationException.class, DateHeader::new);
    }
}
