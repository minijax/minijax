package org.minijax.validation.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.AssertTrue;

public class AssertTrueValidator implements ConstraintValidator<AssertTrue, Boolean> {
    public static final AssertTrueValidator INSTANCE = new AssertTrueValidator();

    private AssertTrueValidator() {
    }

    @Override
    public boolean isValid(final Boolean value, final ConstraintValidatorContext context) {
        return value == null || value.booleanValue();
    }
}
