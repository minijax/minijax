package org.minijax.validation.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;

public class MinValidator implements ConstraintValidator<Min, Number> {
    private final Min min;

    public MinValidator(final Min min) {
        this.min = min;
    }

    @Override
    public boolean isValid(final Number value, final ConstraintValidatorContext context) {
        return value == null || value.longValue() >= min.value();
    }
}
