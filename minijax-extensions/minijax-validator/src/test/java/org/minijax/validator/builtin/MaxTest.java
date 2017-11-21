package org.minijax.validator.builtin;

import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Max;

import org.junit.BeforeClass;
import org.junit.Test;

public class MaxTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static class A {
        @Max(10)
        public Integer a;
        public A(final Integer a) {
            this.a = a;
        }
    }

    public static class B {
        @Max(10)
        public int b;
        public B(final int b) {
            this.b = b;
        }
    }

    public static class C {
        @Max(10)
        public boolean c;
    }

    @Test
    public void testMin() {
        assertTrue(validator.validate(new A(null)).isEmpty());
        assertTrue(validator.validate(new A(0)).isEmpty());
        assertTrue(validator.validate(new A(10)).isEmpty());
        assertTrue(validator.validate(new A(Integer.MIN_VALUE)).isEmpty());
        assertTrue(validator.validate(new B(0)).isEmpty());
        assertTrue(validator.validate(new B(10)).isEmpty());
        assertTrue(validator.validate(new B(Integer.MIN_VALUE)).isEmpty());
        assertEquals(1, validator.validate(new A(11)).size());
        assertEquals(1, validator.validate(new A(Integer.MAX_VALUE)).size());
        assertEquals(1, validator.validate(new B(11)).size());
        assertEquals(1, validator.validate(new B(Integer.MAX_VALUE)).size());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidType() {
        final C b = new C();
        validator.validate(b).size();
    }
}
