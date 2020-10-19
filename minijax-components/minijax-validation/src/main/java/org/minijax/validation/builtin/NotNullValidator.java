package org.minijax.validation.builtin;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;

public class NotNullValidator implements ConstraintValidator<NotNull, Object> {
    public static final NotNullValidator INSTANCE = new NotNullValidator();

    private NotNullValidator() {
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        return value != null;
    }
}
