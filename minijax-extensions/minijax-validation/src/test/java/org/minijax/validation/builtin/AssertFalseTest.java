package org.minijax.validation.builtin;

import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.AssertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

public class AssertFalseTest {
    private static Validator validator;

    @BeforeClass
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
    public void testAssertFalse() {
        assertTrue(validator.validate(new A(null)).isEmpty());
        assertTrue(validator.validate(new A(false)).isEmpty());
        assertEquals(1, validator.validate(new A(true)).size());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidType() {
        final B b = new B();
        b.b = 10;
        validator.validate(b).size();
    }
}
