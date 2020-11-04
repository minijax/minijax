package org.minijax.validation.builtin;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.AssertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AssertFalseTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static class A {
        @AssertFalse
        public Boolean a;
        public A(final Boolean a) {
            this.a = a;
        }
    }

    public static class B {
        @AssertFalse
        public int b;
    }

    @Test
    void testAssertFalse() {
        assertTrue(validator.validate(new A(null)).isEmpty());
        assertTrue(validator.validate(new A(false)).isEmpty());
        assertEquals(1, validator.validate(new A(true)).size());
    }

    @Test
    void testInvalidType() {
        assertThrows(ValidationException.class, () -> {
        final B b = new B();
        b.b = 10;
        validator.validate(b).size();
    });
    }
}
