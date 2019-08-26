package org.minijax.validation.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Max;

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
