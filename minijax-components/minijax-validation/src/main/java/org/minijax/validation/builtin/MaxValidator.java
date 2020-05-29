package org.minijax.validation.builtin;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Max;

public class MaxValidator implements ConstraintValidator<Max, Number> {
    private final Max max;

    public MaxValidator(final Max max) {
        this.max = max;
    }

    @Override
    public boolean isValid(final Number value, final ConstraintValidatorContext context) {
        return value == null || value.longValue() <= max.value();
    }
}
