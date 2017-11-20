package org.minijax.validator.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;

public class IntegerMinValidator implements ConstraintValidator<Min, Integer> {
    private final Min min;

    public IntegerMinValidator(final Min min) {
        this.min = min;
    }

    @Override
    public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
        return value >= min.value();
    }
}
