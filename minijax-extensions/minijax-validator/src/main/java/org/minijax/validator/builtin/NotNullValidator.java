package org.minijax.validator.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

public class NotNullValidator implements ConstraintValidator<NotNull, Object> {
    private static final NotNullValidator INSTANCE = new NotNullValidator();

    public static NotNullValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        return value != null;
    }
}
