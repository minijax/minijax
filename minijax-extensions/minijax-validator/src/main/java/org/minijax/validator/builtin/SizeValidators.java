package org.minijax.validator.builtin;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;

public class SizeValidators {

    SizeValidators() {
        throw new UnsupportedOperationException();
    }


    public static class SizeValidatorForArray implements ConstraintValidator<Size, Object> {
        private final Size size;

        public SizeValidatorForArray(final Size size) {
            this.size = size;
        }

        @Override
        public boolean isValid(final Object value, final ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            final int length = Array.getLength(value);
            return length >= size.min() && length <= size.max();
        }
    }


    public static class SizeValidatorForCharSequence implements ConstraintValidator<Size, CharSequence> {
        private final Size size;

        public SizeValidatorForCharSequence(final Size size) {
            this.size = size;
        }

        @Override
        public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
            return value == null || (value.length() >= size.min() && value.length() <= size.max());
        }
    }


    public static class SizeValidatorForCollection implements ConstraintValidator<Size, Collection<?>> {
        private final Size size;

        public SizeValidatorForCollection(final Size size) {
            this.size = size;
        }

        @Override
        public boolean isValid(final Collection<?> value, final ConstraintValidatorContext context) {
            return value == null || (value.size() >= size.min() && value.size() <= size.max());
        }
    }


    public static class SizeValidatorForMap implements ConstraintValidator<Size, Map<?, ?>> {
        private final Size size;

        public SizeValidatorForMap(final Size size) {
            this.size = size;
        }

        @Override
        public boolean isValid(final Map<?, ?> value, final ConstraintValidatorContext context) {
            return value == null || (value.size() >= size.min() && value.size() <= size.max());
        }
    }
}
