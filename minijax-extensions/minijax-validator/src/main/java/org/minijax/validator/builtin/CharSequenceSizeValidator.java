package org.minijax.validator.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;

public class CharSequenceSizeValidator implements ConstraintValidator<Size, CharSequence> {
    private final Size size;

    public CharSequenceSizeValidator(final Size size) {
        this.size = size;
    }

    @Override
    public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
        return value == null || (value.length() >= size.min() && value.length() <= size.max());
    }
}
