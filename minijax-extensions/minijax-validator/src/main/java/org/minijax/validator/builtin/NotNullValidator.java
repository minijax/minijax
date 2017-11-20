package org.minijax.validator.builtin;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {
    @SuppressWarnings("rawtypes")
    private static final NotNullValidator INSTANCE = new NotNullValidator();

    @SuppressWarnings("unchecked")
    public static <A extends Annotation, T> NotNullValidator<A, T> getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isValid(final T value, final ConstraintValidatorContext context) {
        return value != null;
    }
}
