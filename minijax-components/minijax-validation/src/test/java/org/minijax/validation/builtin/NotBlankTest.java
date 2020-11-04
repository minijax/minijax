package org.minijax.validation.builtin;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class NotBlankTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static class A {
        @NotBlank
        public String a;
    }

    public static class B {
        @NotBlank
        public int b;
    }

    @Test
    void testNullString() {
        final A a = new A();
        a.a = null;
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    void testBlankString() {
        final A a = new A();
        a.a = "";
        assertEquals(1, validator.validate(a).size());
    }

    @Test
    void testValidString() {
        final A a = new A();
        a.a = "a";
        assertTrue(validator.validate(a).isEmpty());
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
