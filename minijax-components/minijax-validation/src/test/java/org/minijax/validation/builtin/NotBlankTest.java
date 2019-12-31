package org.minijax.validation.builtin;

import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;

import org.junit.BeforeClass;
import org.junit.Test;

public class NotBlankTest {
    private static Validator validator;

    @BeforeClass
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

    @Test(expected = ValidationException.class)
    public void testInvalidType() {
        final B b = new B();
        b.b = 10;
        validator.validate(b).size();
    }
}
