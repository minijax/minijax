package org.minijax.validator.builtin;

import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Size;

import org.junit.BeforeClass;
import org.junit.Test;

public class SizeTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static class A {
        @Size(min = 2, max = 4)
        public String a;
    }

    public static class B {
        @Size(min = 2, max = 4)
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
    public void testTooShortString() {
        final A a = new A();
        a.a = "a";
        assertEquals(1, validator.validate(a).size());
    }

    @Test
    public void testValidLowerBoundString() {
        final A a = new A();
        a.a = "aa";
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    public void testValidUpperBoundString() {
        final A a = new A();
        a.a = "aaaa";
        assertTrue(validator.validate(a).isEmpty());
    }

    @Test
    public void testTooLongString() {
        final A a = new A();
        a.a = "aaaaa";
        assertEquals(1, validator.validate(a).size());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidType() {
        final B b = new B();
        b.b = 10;
        validator.validate(b).size();
    }
}
