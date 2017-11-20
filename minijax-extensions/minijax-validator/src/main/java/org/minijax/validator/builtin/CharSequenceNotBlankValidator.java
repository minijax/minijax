package org.minijax.validator.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotBlank;

public class CharSequenceNotBlankValidator implements ConstraintValidator<NotBlank, CharSequence> {
    private static final CharSequenceNotBlankValidator INSTANCE = new CharSequenceNotBlankValidator();

    public static CharSequenceNotBlankValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
        return value == null || value.length() > 0;
    }
}
