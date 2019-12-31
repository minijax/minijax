package org.minijax.validation.builtin;

import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Min;

import org.junit.BeforeClass;
import org.junit.Test;

public class MinTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static class A {
        @Min(10)
        public Integer a;
        public A(final Integer a) {
            this.a = a;
        }
    }

    public static class B {
        @Min(10)
        public int b;
        public B(final int b) {
            this.b = b;
        }
    }

    public static class C {
        @Min(10)
        public boolean c;
    }

    @Test
    public void testMin() {
        assertTrue(validator.validate(new A(null)).isEmpty());
        assertTrue(validator.validate(new A(10)).isEmpty());
        assertTrue(validator.validate(new A(Integer.MAX_VALUE)).isEmpty());
        assertTrue(validator.validate(new B(10)).isEmpty());
        assertTrue(validator.validate(new B(Integer.MAX_VALUE)).isEmpty());
        assertEquals(1, validator.validate(new A(0)).size());
        assertEquals(1, validator.validate(new A(9)).size());
        assertEquals(1, validator.validate(new A(Integer.MIN_VALUE)).size());
        assertEquals(1, validator.validate(new B(0)).size());
        assertEquals(1, validator.validate(new B(9)).size());
        assertEquals(1, validator.validate(new B(Integer.MIN_VALUE)).size());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidType() {
        final C b = new C();
        validator.validate(b).size();
    }
}
