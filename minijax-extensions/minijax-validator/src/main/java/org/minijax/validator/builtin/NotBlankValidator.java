package org.minijax.validator.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotBlank;

public class NotBlankValidator implements ConstraintValidator<NotBlank, CharSequence> {
    public static final NotBlankValidator INSTANCE = new NotBlankValidator();

    private NotBlankValidator() {
    }

    @Override
    public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
        return value == null || value.length() > 0;
    }
}
