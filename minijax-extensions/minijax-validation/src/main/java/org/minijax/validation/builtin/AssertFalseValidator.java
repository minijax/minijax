package org.minijax.validation.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.AssertFalse;

public class AssertFalseValidator implements ConstraintValidator<AssertFalse, Boolean> {
    public static final AssertFalseValidator INSTANCE = new AssertFalseValidator();

    private AssertFalseValidator() {
    }

    @Override
    public boolean isValid(final Boolean value, final ConstraintValidatorContext context) {
        return value == null || !value.booleanValue();
    }
}
