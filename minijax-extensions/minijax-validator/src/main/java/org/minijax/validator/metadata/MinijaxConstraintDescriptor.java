package org.minijax.validator.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;

import org.minijax.validator.builtin.CharSequenceNotBlankValidator;
import org.minijax.validator.builtin.CharSequencePatternValidator;
import org.minijax.validator.builtin.CharSequenceSizeValidator;
import org.minijax.validator.builtin.IntegerMinValidator;
import org.minijax.validator.builtin.NotNullValidator;

public class MinijaxConstraintDescriptor<T extends Annotation> implements ConstraintDescriptor<T> {
    private final T annotation;
    private final ConstraintValidator<T, ?> validator;
    private final String messageTemplate;

    private MinijaxConstraintDescriptor(final T annotation, final ConstraintValidator<T, ?> validator) {
        this.annotation = annotation;
        this.validator = validator;
        messageTemplate = getMessageTemplate(annotation);
    }

    @SuppressWarnings("rawtypes")
    public ConstraintValidator getValidator() {
        validator.initialize(annotation);
        return validator;
    }

    @Override
    public T getAnnotation() {
        return annotation;
    }

    @Override
    public List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidatorClasses() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }

    @Override
    public Set<Class<?>> getGroups() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Class<? extends Payload>> getPayload() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConstraintTarget getValidationAppliesTo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ConstraintDescriptor<?>> getComposingConstraints() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReportAsSingleViolation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidateUnwrappedValue getValueUnwrapping() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> U unwrap(final Class<U> type) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> MinijaxConstraintDescriptor<T> build(final AnnotatedType annotatedType, final T annotation) {
        final Constraint constraint = annotation.annotationType().getAnnotation(Constraint.class);
        if (constraint == null) {
            return null;
        }

        final Class<?> valueClass = (Class<?>) annotatedType.getType();
        final Class<?> annotationClass = annotation.annotationType();

        if (constraint.validatedBy().length > 0) {
            return buildDeclaredValidator(annotation, constraint.validatedBy()[0]);

        } else if (annotationClass == Min.class) {
            return (MinijaxConstraintDescriptor<T>) buildMinValidator((Min) annotation, valueClass);

        } else if (annotationClass == NotBlank.class) {
            return (MinijaxConstraintDescriptor<T>) buildNotBlankValidator((NotBlank) annotation, valueClass);

        } else if (annotationClass == NotNull.class) {
            return (MinijaxConstraintDescriptor<T>) buildNotNullValidator((NotNull) annotation);

        } else if (annotationClass == Pattern.class) {
            return (MinijaxConstraintDescriptor<T>) buildPatternValidator((Pattern) annotation, valueClass);

        } else if (annotationClass == Size.class) {
            return (MinijaxConstraintDescriptor<T>) buildSizeValidator((Size) annotation, valueClass);

        } else {
            throw new ValidationException("Unrecognized constraint annotation: " + annotation);
        }
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T extends Annotation> MinijaxConstraintDescriptor<T> buildDeclaredValidator(final T annotation, final Class validatedBy) {
        final Class<? extends ConstraintValidator<T, ?>> c = validatedBy;
        try {
            return new MinijaxConstraintDescriptor<>(annotation, c.getConstructor().newInstance());
        } catch (final ReflectiveOperationException ex) {
            throw new ValidationException(ex);
        }
    }


    private static MinijaxConstraintDescriptor<Min> buildMinValidator(final Min min, final Class<?> valueClass) {
        if (valueClass == int.class || valueClass == Integer.class) {
            return new MinijaxConstraintDescriptor<>(min, new IntegerMinValidator(min));
        }

        throw new ValidationException("Unsupported type for @Min annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<NotBlank> buildNotBlankValidator(final NotBlank notBlank, final Class<?> valueClass) {
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(notBlank, CharSequenceNotBlankValidator.getInstance());
        }

        throw new ValidationException("Unsupported type for @Pattern annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<NotNull> buildNotNullValidator(final NotNull notNull) {
        return new MinijaxConstraintDescriptor<>(notNull, NotNullValidator.getInstance());
    }


    private static MinijaxConstraintDescriptor<Pattern> buildPatternValidator(final Pattern pattern, final Class<?> valueClass) {
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(pattern, new CharSequencePatternValidator(pattern));
        }

        throw new ValidationException("Unsupported type for @Pattern annotation: " + valueClass);
    }


    private static MinijaxConstraintDescriptor<Size> buildSizeValidator(final Size size, final Class<?> valueClass) {
        if (CharSequence.class.isAssignableFrom(valueClass)) {
            return new MinijaxConstraintDescriptor<>(size, new CharSequenceSizeValidator(size));
        }

        throw new ValidationException("Unsupported type for @Size annotation: " + valueClass);
    }


    private static String getMessageTemplate(final Annotation annotation) {
        try {
            return (String) annotation.annotationType().getMethod("message").invoke(annotation);
        } catch (final ReflectiveOperationException ex) {
            return null;
        }
    }
}
