package org.minijax.validation.builtin;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.AssertTrue;

public class AssertTrueValidator implements ConstraintValidator<AssertTrue, Boolean> {
    public static final AssertTrueValidator INSTANCE = new AssertTrueValidator();

    private AssertTrueValidator() {
    }

    @Override
    public boolean isValid(final Boolean value, final ConstraintValidatorContext context) {
        return value == null || value;
    }
}
