package org.minijax.validation.builtin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Pattern;

public class PatternValidator implements ConstraintValidator<Pattern, CharSequence> {
    private final java.util.regex.Pattern pattern;

    public PatternValidator(final Pattern pattern) {
        this.pattern = java.util.regex.Pattern.compile(pattern.regexp());
    }

    @Override
    public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
        return value == null || pattern.matcher(value).matches();
    }
}
