package org.minijax.validator.builtin;

import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.AssertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class AssertTrueTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static class A {
        @AssertTrue
        public Boolean a;
        public A(final Boolean a) {
            this.a = a;
        }
    }

    public static class B {
        @AssertTrue
        public int b;
    }

    @Test
    public void testAssertTrue() {
        assertTrue(validator.validate(new A(null)).isEmpty());
        assertTrue(validator.validate(new A(true)).isEmpty());
        assertEquals(1, validator.validate(new A(false)).size());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidType() {
        final B b = new B();
        b.b = 10;
        validator.validate(b).size();
    }
}
