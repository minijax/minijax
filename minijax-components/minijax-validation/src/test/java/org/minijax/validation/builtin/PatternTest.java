package org.minijax.validation.builtin;

import static org.junit.Assert.*;

import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;

public class PatternTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static class A {
        @Pattern(regexp = "[a-z]+")
        public String a;
    }

    public static class B {
        @Pattern(regexp = "[a-z]+")
        public int b;
    }

    @Test
    public void testNullString() {
        final A a = new A();
        a.a = null;
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    public void testBlankString() {
        final A a = new A();
        a.a = "";
        assertEquals(1, validator.validate(a).size());
    }

    @Test
    public void testValidString() {
        final A a = new A();
        a.a = "a";
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    public void testMismatchString() {
        final A a = new A();
        a.a = "1";
        assertEquals(1, validator.validate(a).size());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidType() {
        final B b = new B();
        b.b = 10;
        validator.validate(b).size();
    }
}
