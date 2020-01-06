package org.minijax.validation.builtin;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotEmpty;

public class NotEmptyValidators {

    NotEmptyValidators() {
        throw new UnsupportedOperationException();
    }

    public static class NotEmptyValidatorForArray implements ConstraintValidator<NotEmpty, Object> {
        public static final NotEmptyValidatorForArray INSTANCE = new NotEmptyValidatorForArray();

        private NotEmptyValidatorForArray() {
        }

        @Override
        public boolean isValid(final Object value, final ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            return Array.getLength(value) > 0;
        }
    }

    public static class NotEmptyValidatorForCharSequence implements ConstraintValidator<NotEmpty, CharSequence> {
        public static final NotEmptyValidatorForCharSequence INSTANCE = new NotEmptyValidatorForCharSequence();

        private NotEmptyValidatorForCharSequence() {
        }

        @Override
        public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
            return value == null || value.length() > 0;
        }
    }

    public static class NotEmptyValidatorForCollection implements ConstraintValidator<NotEmpty, Collection<?>> {
        public static final NotEmptyValidatorForCollection INSTANCE = new NotEmptyValidatorForCollection();

        private NotEmptyValidatorForCollection() {
        }

        @Override
        public boolean isValid(final Collection<?> value, final ConstraintValidatorContext context) {
            return value == null || !value.isEmpty();
        }
    }

    public static class NotEmptyValidatorForMap implements ConstraintValidator<NotEmpty, Map<?, ?>> {
        public static final NotEmptyValidatorForMap INSTANCE = new NotEmptyValidatorForMap();

        private NotEmptyValidatorForMap() {
        }

        @Override
        public boolean isValid(final Map<?, ?> value, final ConstraintValidatorContext context) {
            return value == null || !value.isEmpty();
        }
    }
}
